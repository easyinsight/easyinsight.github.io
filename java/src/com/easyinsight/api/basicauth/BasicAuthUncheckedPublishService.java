package com.easyinsight.api.basicauth;

import com.easyinsight.api.UncheckedPublishService;

import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.cxf.jaxws.context.WebServiceContextImpl;
import org.apache.cxf.jaxws.context.WrappedMessageContext;

/**
 * User: James Boe
 * Date: Jan 29, 2009
 * Time: 1:33:12 PM
 */
public class BasicAuthUncheckedPublishService extends UncheckedPublishService implements IUncheckedPublishService {

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
