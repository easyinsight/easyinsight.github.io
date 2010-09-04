package com.easyinsight.api;

import com.easyinsight.api.dynamic.DynamicDeploymentUnit;
import com.easyinsight.api.dynamic.DynamicServiceDefinition;
import com.easyinsight.api.dynamic.APIPasswordCallback;
import com.easyinsight.api.basicauth.BasicAuthUncheckedPublishService;
import com.easyinsight.api.basicauth.BasicAuthAuthorizationInterceptor;
import com.easyinsight.api.basicauth.BasicAuthValidatingPublishService;
import com.easyinsight.api.v2.BasicAuthEIV2API;
import com.easyinsight.api.v2.BasicAuthTransactionalLoadAPI;
import com.easyinsight.api.v2.EIV2API;
import com.easyinsight.api.v2.TransactionalLoadAPI;
import com.easyinsight.api.wsdeathstar.WSDeathStarUncheckedPublishService;
import com.easyinsight.api.wsdeathstar.WSDeathStarValidatingPublishService;
import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;

import javax.xml.ws.Endpoint;
import java.util.Map;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.apache.ws.security.WSConstants;

/**
 * User: James Boe
 * Date: Aug 28, 2008
 * Time: 12:25:36 PM
 */
public class APIManager implements IAPIManager {

    private static APIManager instance;

    private Map<Long, DynamicDeploymentUnit> deployedDynamicServices = new HashMap<Long, DynamicDeploymentUnit>();

    public void start() {
        try {
            instance = this;
            createUncheckedSOAPAPI();
            createdValidatedSOAPAPI();
            createV2API();
            go();
        } catch (Exception e) {
            LogClass.error(e);
        }
    }

    private void createV2API() {
        EIV2API basicAuthPublishService = new BasicAuthEIV2API();
        EndpointImpl basicAuthEndpoint = (EndpointImpl) Endpoint.create(basicAuthPublishService);
        basicAuthEndpoint.setPublishedEndpointUrl("https://www.easy-insight.com/app/services/EIDataV2");
        basicAuthEndpoint.publish("/EIDataV2");
        configureBasicAuth(basicAuthEndpoint);

        TransactionalLoadAPI transactionalLoadAPI = new BasicAuthTransactionalLoadAPI();
        EndpointImpl transactionalLoadEndpoint = (EndpointImpl) Endpoint.create(transactionalLoadAPI);
        transactionalLoadEndpoint.setPublishedEndpointUrl("https://www.easy-insight.com/app/services/EITransactionalLoad");
        transactionalLoadEndpoint.publish("/EITransactionalLoad");
        configureBasicAuth(transactionalLoadEndpoint);
    }

    private void createdValidatedSOAPAPI() {        
        ValidatingPublishService basicAuthPublishService = new BasicAuthValidatingPublishService();
        EndpointImpl basicAuthEndpoint = (EndpointImpl) Endpoint.create(basicAuthPublishService);
        basicAuthEndpoint.setPublishedEndpointUrl("https://www.easy-insight.com/app/services/ValidatedPublishBasic");
        basicAuthEndpoint.publish("/ValidatedPublishBasic");
        configureBasicAuth(basicAuthEndpoint);

        ValidatingPublishService wsDeathStarPublishService = new WSDeathStarValidatingPublishService();
        EndpointImpl wsDeathStarEndpoint = (EndpointImpl) Endpoint.create(wsDeathStarPublishService);
        wsDeathStarEndpoint.setPublishedEndpointUrl("https://www.easy-insight.com/app/services/ValidatedPublishWSS");
        wsDeathStarEndpoint.publish("/ValidatedPublishWSS");
        configureWSDeathStar(wsDeathStarEndpoint);
    }

    private void createUncheckedSOAPAPI() {
        UncheckedPublishService basicAuthPublishService = new BasicAuthUncheckedPublishService();
        EndpointImpl basicAuthEndpoint = (EndpointImpl) Endpoint.create(basicAuthPublishService);
        basicAuthEndpoint.setPublishedEndpointUrl("https://www.easy-insight.com/app/services/UncheckedPublishBasic");
        basicAuthEndpoint.publish("/UncheckedPublishBasic");
        configureBasicAuth(basicAuthEndpoint);
        UncheckedPublishService wsDeathStarPublishService = new WSDeathStarUncheckedPublishService();
        EndpointImpl wsDeathStarEndpoint = (EndpointImpl) Endpoint.create(wsDeathStarPublishService);
        wsDeathStarEndpoint.setPublishedEndpointUrl("https://www.easy-insight.com/app/services/UncheckedPublishWSS");
        wsDeathStarEndpoint.publish("/UncheckedPublishWSS");
        configureWSDeathStar(wsDeathStarEndpoint);
    }

    private void configureBasicAuth(EndpointImpl endpointImpl) {
        org.apache.cxf.endpoint.Endpoint endpoint = endpointImpl.getServer().getEndpoint();
        BasicAuthAuthorizationInterceptor basicAuthInterceptor = new BasicAuthAuthorizationInterceptor();
        endpoint.getInInterceptors().add(basicAuthInterceptor);
        endpoint.getInInterceptors().add(new MessageThrottlingInterceptor());
    }

    private void configureWSDeathStar(EndpointImpl endpointImpl) {
        org.apache.cxf.endpoint.Endpoint endpoint = endpointImpl.getServer().getEndpoint();
        Map<String, Object> securityInProps = new HashMap<String, Object>();
        securityInProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
        securityInProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
        securityInProps.put(WSHandlerConstants.PW_CALLBACK_CLASS, APIPasswordCallback.class.getName());
        WSS4JInInterceptor wssIn = new WSS4JInInterceptor(securityInProps);
        endpoint.getInInterceptors().add(wssIn);
        endpoint.getInInterceptors().add(new MessageThrottlingInterceptor());
    }

    private void go() {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement feedQueryStmt = conn.prepareStatement("SELECT DYNAMIC_SERVICE_DESCRIPTOR_ID, FEED_ID FROM " +
                    "DYNAMIC_SERVICE_DESCRIPTOR");
            ResultSet rs = feedQueryStmt.executeQuery();
            while (rs.next()) {
                DynamicServiceDefinition definition = new DynamicServiceDefinition(rs.getLong(2), rs.getLong(1));
                definition.deploy(conn, this);
            }
        } catch (SQLException e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public static APIManager instance() {
        return instance;
    }

    public void dynamicDeployment(DynamicDeploymentUnit unit) {
        unit.deploy();
        deployedDynamicServices.put(unit.getFeedID(), unit);
    }

    public void undeploy(long feedID) {
        DynamicDeploymentUnit unit = deployedDynamicServices.get(feedID);
        if (unit != null) {
            unit.undeploy();
        }
    }
}
