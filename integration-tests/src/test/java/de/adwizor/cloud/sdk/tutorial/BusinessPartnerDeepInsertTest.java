package de.adwizor.cloud.sdk.tutorial;

import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartner;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.DefaultBusinessPartnerService;
import com.sap.cloud.sdk.testutil.MockUtil;
import io.restassured.RestAssured;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URISyntaxException;
import java.net.URL;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

@RunWith(Arquillian.class)
public class BusinessPartnerDeepInsertTest {

    private static final MockUtil mockUtil = new MockUtil();

    @ArquillianResource
    private URL baseUrl;

    @Deployment
    public static WebArchive createDeployment() {
        return TestUtil.createDeployment(BusinessPartnerServlet.class,
                BusinessPartner.class,
                StoreBusinessPartnerCommand.class,
                DefaultBusinessPartnerService.class);
    }

    @BeforeClass
    public static void beforeClass() throws URISyntaxException {
        mockUtil.mockDefaults();
        mockUtil.mockErpDestination("ErpQueryEndpoint", "ERP_TEST_SYSTEM");
    }

    @Before
    public void before() {
        RestAssured.baseURI = baseUrl.toExternalForm();
    }

    @Test
    public void testStoreAndGetCustomers() {

        given()
                .parameters("firstname", "John",
                        "lastname", "Doe",
                        "country", "US",
                        "city", "Tuxedo",
                        "email", "john@doe.com")
                .when()
                    .post("/businesspartners")
                .then()
                    .log().all()
                    .statusCode(201)
                    .and()
                    .body("BusinessPartner", not(isEmptyString()))
                    .and()
                    .body("BusinessPartner", not(isEmptyString()));


    }
}
