package com.easyinsight.api.v2;

import org.apache.cxf.jaxws.context.WrappedMessageContext;

import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;

/**
 * User: jamesboe
 * Date: Sep 3, 2010
 * Time: 6:42:21 PM
 */
public class BasicAuthTransactionalLoadAPI extends TransactionalLoadAPI {
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
