package com.easyinsight.api;

import com.easyinsight.api.dynamic.DynamicDeploymentUnit;
import com.easyinsight.api.dynamic.DynamicServiceDefinition;
import com.easyinsight.api.dynamic.APIPasswordCallback;
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
public class APIManager {

    private static APIManager instance;

    private Map<Long, DynamicDeploymentUnit> deployedDynamicServices = new HashMap<Long, DynamicDeploymentUnit>();

    public void start() {
        try {
            instance = this;
            UncheckedPublishService publishService = new UncheckedPublishService();
            EndpointImpl endpointImpl = (EndpointImpl) Endpoint.publish("/UncheckedPublish", publishService);
            ValidatingPublishService validatingPublishService = new ValidatingPublishService();
            Endpoint.publish("/ValidatingPublish", validatingPublishService);
            org.apache.cxf.endpoint.Endpoint endpoint = endpointImpl.getServer().getEndpoint();
            Map<String, Object> securityInProps = new HashMap<String, Object>();
            securityInProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
            securityInProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
            securityInProps.put(WSHandlerConstants.PW_CALLBACK_CLASS, APIPasswordCallback.class.getName());
            WSS4JInInterceptor wssIn = new WSS4JInInterceptor(securityInProps);
            endpoint.getInInterceptors().add(wssIn);
            go();
        } catch (Exception e) {
            LogClass.error(e);
        }
    }

    private void go() {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement feedQueryStmt = conn.prepareStatement("SELECT DYNAMIC_SERVICE_DESCRIPTOR_ID, FEED_ID FROM " +
                    "DYNAMIC_SERVICE_DESCRIPTOR");
            ResultSet rs = feedQueryStmt.executeQuery();
            while (rs.next()) {
                DynamicServiceDefinition definition = new DynamicServiceDefinition(rs.getLong(2), rs.getLong(1));
                definition.deploy(conn);
            }
        } catch (SQLException e) {
            LogClass.error(e);
        } finally {
            Database.instance().closeConnection(conn);
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
