package com.easyinsight.api.dynamic;

import org.apache.ws.security.WSPasswordCallback;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;

import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.PasswordService;

/**
 * User: James Boe
 * Date: Sep 15, 2008
 * Time: 10:46:15 AM
 */
public class APIPasswordCallback implements CallbackHandler {
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
        try {
            SecurityUtil.authenticate(pc.getIdentifer(), pc.getPassword());
        } catch (Exception e) {
            throw new SecurityException();
        }
    }
}
