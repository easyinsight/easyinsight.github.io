package com.easyinsight.api.basicauth;

import com.easyinsight.api.ValidatingPublishService;

import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;

import org.apache.cxf.jaxws.context.WrappedMessageContext;

/**
 * User: James Boe
 * Date: Feb 3, 2009
 * Time: 9:21:34 PM
 */
public class BasicAuthValidatingPublishService extends ValidatingPublishService implements IValidatingPublishService {
    @Resource
    private WebServiceContext context;

    protected String getUserName() {
        WrappedMessageContext wrappedContext = (WrappedMessageContext) context.getMessageContext();
        return (String) wrappedContext.get("javax.xml.ws.security.auth.username");
    }
}
