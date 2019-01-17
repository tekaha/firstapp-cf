package de.adwizor.cloud.sdk.tutorial;

import com.google.gson.Gson;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.sap.cloud.sdk.s4hana.connectivity.ErpConfigContext;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.AddressEmailAddress;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartnerAddress;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartnerRole;
import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.sap.cloud.sdk.cloudplatform.logging.CloudLoggerFactory;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;

import com.sap.cloud.sdk.s4hana.datamodel.odata.helper.Order;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartner;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.DefaultBusinessPartnerService;

@WebServlet("/businesspartners")
public class BusinessPartnerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = CloudLoggerFactory.getLogger(BusinessPartnerServlet.class);

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        try {
            final List<BusinessPartner> businessPartners =
                    new GetBusinessPartnersCommand(new ErpConfigContext(), new DefaultBusinessPartnerService()).execute();
            response.setContentType("application/json");
            response.getWriter().write(new Gson().toJson(businessPartners));

        } catch (final Exception e) {
            logger.error(e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        final String firstname = request.getParameter("firstname");
        final String lastname = request.getParameter("lastname");
        final String country = request.getParameter("country");
        final String city = request.getParameter("city");
        final String email = request.getParameter("email");

        // do consistency checks here...

        final AddressEmailAddress emailAddress = AddressEmailAddress.builder()
                .emailAddress(email)
                .build();

        final BusinessPartnerAddress businessPartnerAddress = BusinessPartnerAddress.builder()
                .country(country)
                .cityName(city)
                .emailAddress(emailAddress)
                .build();

        final BusinessPartnerRole businessPartnerRole = BusinessPartnerRole.builder()
                .businessPartnerRole("FLCU01")
                .build();

        final BusinessPartner businessPartner = BusinessPartner.builder()
                .firstName(firstname)
                .lastName(lastname)
                .businessPartnerCategory("1")
                .correspondenceLanguage("EN")
                .businessPartnerAddress(businessPartnerAddress)
                .businessPartnerRole(businessPartnerRole)
                .build();

        String responseBody;

        try {

            final BusinessPartner storedBusinessPartner = new StoreBusinessPartnerCommand(new ErpConfigContext(), new DefaultBusinessPartnerService(), businessPartner).execute();
            responseBody = new Gson().toJson(storedBusinessPartner);
            response.setStatus(HttpServletResponse.SC_CREATED);

        } catch (final HystrixBadRequestException e) {
            responseBody = e.getMessage();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.error(e.getMessage(), e);
        }

        response.setContentType("application/json");
        response.getOutputStream().print(responseBody);

    }
}