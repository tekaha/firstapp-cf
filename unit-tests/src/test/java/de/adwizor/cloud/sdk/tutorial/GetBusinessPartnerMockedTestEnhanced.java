package de.adwizor.cloud.sdk.tutorial;

import com.google.common.collect.Lists;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.odatav2.connectivity.ODataExceptionType;
import com.sap.cloud.sdk.s4hana.connectivity.ErpConfigContext;
import com.sap.cloud.sdk.s4hana.datamodel.odata.helper.ExpressionFluentHelper;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartner;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.selectable.BusinessPartnerSelectable;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.BusinessPartnerService;
import com.sap.cloud.sdk.testutil.MockUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URI;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class GetBusinessPartnerMockedTestEnhanced {

    private static final MockUtil mockUtil = new MockUtil();

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private BusinessPartnerService service;

    @Mock
    private BusinessPartner alice;

    @Mock
    private BusinessPartner bob;

    @Before
    public void before() {
        mockUtil.mockDefaults();
        mockUtil.mockDestination("ErpQueryEndpoint", URI.create(""));

        when(alice.getFirstName()).thenReturn("Alice");
        when(bob.getFirstName()).thenReturn("Bob");
    }

    @Test
    public void testGetAnyBusinessPartner() throws Exception {

        when(service.getAllBusinessPartner()
                .filter(any(ExpressionFluentHelper.class))
                .select(any(BusinessPartnerSelectable.class))
                .execute(any(ErpConfigContext.class)))
            .thenReturn(Lists.newArrayList(alice, bob));

        final List<BusinessPartner> businessPartnerList = new GetBusinessPartnersCommand(new ErpConfigContext(), service).execute();

        assertEquals(2, businessPartnerList.size());
        assertEquals("Alice", businessPartnerList.get(0).getFirstName());
        assertEquals("Bob", businessPartnerList.get(1).getFirstName());

    }

    @Test
    public void testGetSpecificBusinessPartner() throws Exception {

        when(service.getAllBusinessPartner()
                .filter(BusinessPartner.IS_NATURAL_PERSON.eq("X"))
                .select(any(BusinessPartnerSelectable.class))
                .execute(any(ErpConfigContext.class)))
            .thenReturn(Lists.newArrayList(alice));

        final List<BusinessPartner> businessPartnerList = new GetBusinessPartnersCommand(new ErpConfigContext(), service).execute();

        assertEquals(1, businessPartnerList.size());
        assertEquals("Alice", businessPartnerList.get(0).getFirstName());
    }

    @Test(expected = HystrixBadRequestException.class)
    public void testGetBusinessPartnerFailure() throws Exception {

        when(service.getAllBusinessPartner()
                .filter(any(ExpressionFluentHelper.class))
                .select(any(BusinessPartnerSelectable.class))
                .execute(any(ErpConfigContext.class)))
            .thenThrow(new ODataException(ODataExceptionType.METADATA_FETCH_FAILED, "Something went wrong", null));

        new GetBusinessPartnersCommand(new ErpConfigContext(), service).execute();
    }
}
