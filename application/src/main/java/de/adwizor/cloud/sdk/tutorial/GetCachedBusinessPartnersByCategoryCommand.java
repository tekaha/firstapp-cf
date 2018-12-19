package de.adwizor.cloud.sdk.tutorial;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.sap.cloud.sdk.cloudplatform.cache.CacheKey;
import com.sap.cloud.sdk.cloudplatform.logging.CloudLoggerFactory;
import com.sap.cloud.sdk.s4hana.connectivity.CachingErpCommand;
import com.sap.cloud.sdk.s4hana.datamodel.odata.helper.Order;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartner;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.DefaultBusinessPartnerService;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GetCachedBusinessPartnersByCategoryCommand extends CachingErpCommand<List<BusinessPartner>> {

    private static final Logger logger = CloudLoggerFactory.getLogger(GetCachedBusinessPartnersByCategoryCommand.class);

    private static final Cache<CacheKey, List<BusinessPartner>> cache =
            CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .concurrencyLevel(10)
                .build();

    private final String category;

    protected GetCachedBusinessPartnersByCategoryCommand(final String category) {
        super(GetCachedBusinessPartnersByCategoryCommand.class);
        this.category = category;
    }

    @Nonnull
    @Override
    protected Cache<CacheKey, List<BusinessPartner>> getCache() {
        return cache;
    }

    @Nonnull
    @Override
    protected CacheKey getCommandCacheKey() {
        return super.getCommandCacheKey().append(category);
    }

    @Nonnull
    @Override
    protected List<BusinessPartner> runCacheable() throws Exception {
        final List<BusinessPartner> businessPartners =
                new DefaultBusinessPartnerService()
                        .getAllBusinessPartner()
                        .select(BusinessPartner.BUSINESS_PARTNER,
                                BusinessPartner.LAST_NAME,
                                BusinessPartner.FIRST_NAME,
                                BusinessPartner.IS_MALE,
                                BusinessPartner.IS_FEMALE,
                                BusinessPartner.CREATION_DATE,
                                BusinessPartner.BUSINESS_PARTNER_CATEGORY)
                        .filter(BusinessPartner.BUSINESS_PARTNER_CATEGORY.eq(category))
                        .orderBy(BusinessPartner.LAST_NAME, Order.ASC)
                        .execute();
        return businessPartners;
    }

    @Override
    protected List<BusinessPartner> getFallback() {
        logger.warn("Fallback called because of exception:", getExecutionException());
        return Collections.emptyList();
    }
}
