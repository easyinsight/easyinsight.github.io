package com.easyinsight.api.dynamic;

import com.easyinsight.logging.LogClass;
import com.easyinsight.api.basicauth.BasicAuthAuthorizationInterceptor;
import com.easyinsight.api.basicauth.DynamicBasicAuthAuthorizationInterceptor;


import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.apache.ws.security.WSConstants;

import javax.xml.ws.Endpoint;
import java.util.HashMap;
import java.util.Map;

/**
 * User: James Boe
 * Date: Sep 1, 2008
 * Time: 5:12:56 PM
 */
public class DynamicDeploymentUnit {
    private DynamicClassLoader dynamicClassLoader;
    private long feedID;
    private Endpoint wssEndpoint;
    private Endpoint basicAuthEndpoint;

    public DynamicDeploymentUnit(DynamicClassLoader dynamicClassLoader, long feedID) {
        this.dynamicClassLoader = dynamicClassLoader;
        this.feedID = feedID;
    }

    public long getFeedID() {
        return feedID;
    }

    public void deploy() {
        try {
            Class clazz = Class.forName("DynamicService", true, dynamicClassLoader);
            Object obj = clazz.newInstance();
            Class.forName("IDynamicService", true, dynamicClassLoader);
            String endpointName = "/swss" + feedID;
            //String address = "http://localhost:9000/" + endpointName;
            EndpointImpl endpointImpl = (EndpointImpl) Endpoint.publish(endpointName, obj);
            wssEndpoint = endpointImpl;
            org.apache.cxf.endpoint.Endpoint endpoint = endpointImpl.getServer().getEndpoint();
            Map<String, Object> securityInProps = new HashMap<String, Object>();
            securityInProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
            securityInProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
            securityInProps.put(WSHandlerConstants.PW_CALLBACK_CLASS, APIPasswordCallback.class.getName());
            WSS4JInInterceptor wssIn = new WSS4JInInterceptor(securityInProps);
            endpoint.getInInterceptors().add(wssIn);
            basicAuthDeployment();
            //Map<String, Object> securityOutProps = new HashMap<String, Object>();
            //WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(securityOutProps);
            //wssEndpoint.getOutInterceptors().add(wssOut);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    private void basicAuthDeployment() throws Exception {
        Class clazz = Class.forName("DynamicService", true, dynamicClassLoader);
        Object obj = clazz.newInstance();
        Class.forName("IDynamicService", true, dynamicClassLoader);
        String endpointName = "/sauth" + feedID;
        //String address = "http://localhost:9000/" + endpointName;
        EndpointImpl endpointImpl = (EndpointImpl) Endpoint.publish(endpointName, obj);
        basicAuthEndpoint = endpointImpl;
        org.apache.cxf.endpoint.Endpoint endpoint = endpointImpl.getServer().getEndpoint();
        DynamicBasicAuthAuthorizationInterceptor basicAuthInterceptor = new DynamicBasicAuthAuthorizationInterceptor(feedID);
        endpoint.getInInterceptors().add(basicAuthInterceptor);
    }

    public void undeploy() {
        wssEndpoint.stop();
        basicAuthEndpoint.stop();
    }
}
