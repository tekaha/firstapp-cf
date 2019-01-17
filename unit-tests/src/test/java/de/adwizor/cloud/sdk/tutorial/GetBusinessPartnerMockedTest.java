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
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.URI;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.when;

public class GetBusinessPartnerMockedTest {

    private static final MockUtil mockUtil = new MockUtil();
    private static BusinessPartner alice;
    private static BusinessPartner bob;

    @BeforeClass
    public static void beforeClass() throws Exception {
        mockUtil.mockDefaults();
        mockUtil.mockDestination("ErpQueryEndpoint", URI.create(""));

        alice = new BusinessPartner();
        alice.setFirstName("Alice");

        bob = new BusinessPartner();
        bob.setFirstName("Bob");
    }

    @Test
    public void testGetAnyBusinessPartner() throws Exception {

        final BusinessPartnerService service = Mockito.mock(BusinessPartnerService.class, RETURNS_DEEP_STUBS);

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

        final BusinessPartnerService service = Mockito.mock(BusinessPartnerService.class, RETURNS_DEEP_STUBS);

        when(service.getAllBusinessPartner()
                .filter(BusinessPartner.IS_NATURAL_PERSON.eq("X"))
                .select(any(BusinessPartnerSelectable.class))
                .execute(any(ErpConfigContext.class)))
                .thenReturn(Lists.newArrayList(alice));

        final List<BusinessPartner> businessParnterList = new GetBusinessPartnersCommand(new ErpConfigContext(), service).execute();

        assertEquals(1, businessParnterList.size());
        assertEquals("Alice", businessParnterList.get(0).getFirstName());

    }

    @Test(expected = HystrixBadRequestException.class)
    public void testGetBusinessPartnerFailure() throws Exception {

        final BusinessPartnerService service = Mockito.mock(BusinessPartnerService.class, RETURNS_DEEP_STUBS);

        when(service.getAllBusinessPartner()
                .filter(any(ExpressionFluentHelper.class))
                .select(any(BusinessPartnerSelectable.class))
                .execute(any(ErpConfigContext.class)))
            .thenThrow(new ODataException(ODataExceptionType.METADATA_FETCH_FAILED, "Something went wrong", null));

        new GetBusinessPartnersCommand(new ErpConfigContext(), service).execute();
    }

}
