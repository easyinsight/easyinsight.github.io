package com.easyinsight.api.v2;

import org.apache.cxf.jaxws.context.WrappedMessageContext;

import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;

/**
 * User: jamesboe
 * Date: Jun 21, 2010
 * Time: 1:39:34 PM
 */
public class BasicAuthEIV2API extends EIV2API implements IEIV2API {
    @Resource
    private WebServiceContext context;

    protected String getUserName() {
        WrappedMessageContext wrappedContext = (WrappedMessageContext) context.getMessageContext();
        return (String) wrappedContext.get("javax.xml.ws.security.auth.username");
    }

    protected long getAccountID() {
        WrappedMessageContext wrappedContext = (WrappedMessageContext) context.getMessageContext();
        return (Long) wrappedContext.get("accountID");
    }

    protected long getUserID() {
        WrappedMessageContext wrappedContext = (WrappedMessageContext) context.getMessageContext();
        return (Long) wrappedContext.get("userID");
    }
}
