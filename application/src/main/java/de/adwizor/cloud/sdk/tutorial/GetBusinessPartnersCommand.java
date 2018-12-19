package de.adwizor.cloud.sdk.tutorial;

import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.sap.cloud.sdk.cloudplatform.logging.CloudLoggerFactory;
import com.sap.cloud.sdk.frameworks.hystrix.HystrixUtil;
import com.sap.cloud.sdk.s4hana.connectivity.ErpCommand;
import com.sap.cloud.sdk.s4hana.datamodel.odata.helper.Order;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartner;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.DefaultBusinessPartnerService;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class GetBusinessPartnersCommand extends ErpCommand<List<BusinessPartner>> {

    private static final Logger logger = CloudLoggerFactory.getLogger(GetBusinessPartnersCommand.class);

    private static final String CATEGORY_PERSON = "1";

    protected GetBusinessPartnersCommand() {
        //super(GetBusinessPartnersCommand.class);
        super(HystrixUtil
                .getDefaultErpCommandSetter(
                        GetBusinessPartnersCommand.class,
                        HystrixUtil.getDefaultErpCommandProperties()
                            .withExecutionTimeoutInMilliseconds(10000)
                )
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                        .withCoreSize(20))
        );
    }

    @Override
    protected List<BusinessPartner> run() throws Exception {
        final List<BusinessPartner> businessPartners =
                new DefaultBusinessPartnerService()
                        .getAllBusinessPartner()
                        .select(BusinessPartner.BUSINESS_PARTNER,
                                BusinessPartner.LAST_NAME,
                                BusinessPartner.FIRST_NAME,
                                BusinessPartner.IS_MALE,
                                BusinessPartner.IS_FEMALE,
                                BusinessPartner.CREATION_DATE)
                        .filter(BusinessPartner.BUSINESS_PARTNER_CATEGORY.eq(CATEGORY_PERSON))
                        .orderBy(BusinessPartner.LAST_NAME, Order.ASC)
                        .top(10)
                        .execute();
        return businessPartners;
    }

    @Override
    protected List<BusinessPartner> getFallback() {
        logger.warn("Fallback called because of exception:", getExecutionException());
        return Collections.emptyList();
    }
}
