package com.easyinsight.api.wsdeathstar;

import com.easyinsight.api.ValidatingPublishService;

import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.util.Vector;

import org.apache.ws.security.handler.WSHandlerResult;
import org.apache.ws.security.WSSecurityEngineResult;
import org.apache.ws.security.WSUsernameTokenPrincipal;

/**
 * User: James Boe
 * Date: Feb 3, 2009
 * Time: 9:24:37 PM
 */
public class WSDeathStarValidatingPublishService extends ValidatingPublishService implements IValidatingPublishService {
    @Resource
    private WebServiceContext context;

    protected String getUserName() {
        MessageContext ctx = context.getMessageContext();
        Vector results = (Vector) ctx.get("RECV_RESULTS");
        WSHandlerResult wsHandlerResult = (WSHandlerResult) results.get(0);
        WSSecurityEngineResult securityResult = (WSSecurityEngineResult) wsHandlerResult.getResults().get(0);
        WSUsernameTokenPrincipal principal = (WSUsernameTokenPrincipal) securityResult.get(WSSecurityEngineResult.TAG_PRINCIPAL);
        return principal.getName();
    }

    protected long getAccountID() {
        throw new UnsupportedOperationException();
    }

    protected long getUserID() {
        throw new UnsupportedOperationException();
    }
}
