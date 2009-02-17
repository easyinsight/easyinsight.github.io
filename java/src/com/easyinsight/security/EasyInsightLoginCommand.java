package com.easyinsight.security;

import flex.messaging.security.LoginCommand;
import flex.messaging.FlexContext;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

import com.easyinsight.users.UserService;
import com.easyinsight.users.UserServiceResponse;

/**
 * User: James Boe
 * Date: Aug 5, 2008
 * Time: 3:35:17 PM
 */
public class EasyInsightLoginCommand implements LoginCommand {

    private UserService userService = new UserService();

    public void start(ServletConfig servletConfig) {

    }

    public void stop() {

    }

    public Principal doAuthentication(String userName, Object credentials) {
        String password = (String) credentials;
        UserServiceResponse userServiceResponse = userService.authenticate(userName, password);
        if (userServiceResponse.isSuccessful()) {
            HttpSession session = FlexContext.getHttpRequest().getSession();
            session.setAttribute("userID", userServiceResponse.getUserID());
            session.setAttribute("accountID", userServiceResponse.getAccountID());
            session.setAttribute("userName", userServiceResponse.getUserName());
            session.setAttribute("accountType", userServiceResponse.getAccountType());
            return new UserPrincipal(userName, userServiceResponse.getAccountID(), userServiceResponse.getUserID(), userServiceResponse.getAccountType());
        } else {
            return null;
        }
    }

    public boolean doAuthorization(Principal principal, List list) {
        return true;
    }

    public boolean logout(Principal principal) {
        return false;
    }
}
