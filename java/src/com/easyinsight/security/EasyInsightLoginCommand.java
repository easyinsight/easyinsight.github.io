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
        UserServiceResponse userServiceResponse = userService.authenticate(userName, password, false);
        if (userServiceResponse.isSuccessful()) {
            HttpSession session = FlexContext.getHttpRequest().getSession();
            SecurityUtil.populateSession(session, userServiceResponse);
            return new UserPrincipal(userServiceResponse.getUserName(), userServiceResponse.getAccountID(), userServiceResponse.getUserID(), userServiceResponse.getAccountType(),
                    userServiceResponse.isAccountAdmin(), userServiceResponse.getFirstDayOfWeek(), userServiceResponse.getPersonaName());
        } else {
            userServiceResponse = userService.sessionCookieCheck(password, userName, true);
            if (userServiceResponse != null && userServiceResponse.isSuccessful()) {
                HttpSession session = FlexContext.getHttpRequest().getSession();
                SecurityUtil.populateSession(session, userServiceResponse);
                return new UserPrincipal(userServiceResponse.getUserName(), userServiceResponse.getAccountID(), userServiceResponse.getUserID(), userServiceResponse.getAccountType(),
                    userServiceResponse.isAccountAdmin(), userServiceResponse.getFirstDayOfWeek(), userServiceResponse.getPersonaName());
            } else {
                userServiceResponse = userService.seleniumCheck(userName, password);
                if (userServiceResponse != null) {
                    HttpSession session = FlexContext.getHttpRequest().getSession();
                    SecurityUtil.populateSession(session, userServiceResponse);
                    return new UserPrincipal(userServiceResponse.getUserName(), userServiceResponse.getAccountID(), userServiceResponse.getUserID(), userServiceResponse.getAccountType(),
                        userServiceResponse.isAccountAdmin(), userServiceResponse.getFirstDayOfWeek(), userServiceResponse.getPersonaName());
                } else {
                    userServiceResponse = userService.htmlEstablish(password, userName);
                    if (userServiceResponse != null) {
                        HttpSession session = FlexContext.getHttpRequest().getSession();
                        SecurityUtil.populateSession(session, userServiceResponse);
                        return new UserPrincipal(userServiceResponse.getUserName(), userServiceResponse.getAccountID(), userServiceResponse.getUserID(), userServiceResponse.getAccountType(),
                                userServiceResponse.isAccountAdmin(), userServiceResponse.getFirstDayOfWeek(), userServiceResponse.getPersonaName());
                    }
                }
            }
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