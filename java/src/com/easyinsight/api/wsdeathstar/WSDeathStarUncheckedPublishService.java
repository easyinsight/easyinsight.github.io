package com.easyinsight.api.wsdeathstar;

import org.apache.ws.security.handler.WSHandlerResult;
import org.apache.ws.security.WSSecurityEngineResult;
import org.apache.ws.security.WSUsernameTokenPrincipal;

import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.WebServiceContext;
import javax.annotation.Resource;
import java.util.Vector;

import com.easyinsight.api.UncheckedPublishService;
import com.easyinsight.api.basicauth.*;

/**
 * User: James Boe
 * Date: Jan 29, 2009
 * Time: 1:33:25 PM
 */
public class WSDeathStarUncheckedPublishService extends UncheckedPublishService implements com.easyinsight.api.basicauth.IUncheckedPublishService {

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
