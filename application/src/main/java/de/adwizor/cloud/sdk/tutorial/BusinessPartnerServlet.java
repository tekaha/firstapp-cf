package de.adwizor.cloud.sdk.tutorial;

import com.google.gson.Gson;
import com.sap.cloud.sdk.s4hana.connectivity.ErpConfigContext;
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
}