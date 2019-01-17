package de.adwizor.cloud.sdk.tutorial;

import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.s4hana.connectivity.ErpCommand;
import com.sap.cloud.sdk.s4hana.connectivity.ErpConfigContext;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartner;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.BusinessPartnerService;

import javax.annotation.Nonnull;

public class StoreBusinessPartnerCommand extends ErpCommand<BusinessPartner> {

    private BusinessPartnerService businessPartnerService;
    private BusinessPartner businessPartner;

    public StoreBusinessPartnerCommand(final ErpConfigContext erpConfigContext, final BusinessPartnerService businessPartnerService, final BusinessPartner businessPartner) {
        super(StoreBusinessPartnerCommand.class, erpConfigContext);
        this.businessPartnerService = businessPartnerService;
        this.businessPartner = businessPartner;
    }

    @Override
    protected BusinessPartner run() throws Exception {

        try {
            return businessPartnerService
                    .createBusinessPartner(businessPartner)
                    .execute(getConfigContext());
        } catch (final ODataException e) {
            throw new HystrixBadRequestException(e.getMessage(), e);
        }
    }
}
