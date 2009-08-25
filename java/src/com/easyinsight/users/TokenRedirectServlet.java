package com.easyinsight.users;

import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.util.AuthenticationException;
import com.easyinsight.security.SecurityUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.Iterator;

/**
 * User: jamesboe
 * Date: Aug 24, 2009
 * Time: 10:08:18 AM
 */
public class TokenRedirectServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {

        // Retrieve the AuthSub token assigned by Google
        String token = AuthSubUtil.getTokenFromReply(req.getQueryString());
        if (token == null) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                         "No token specified.");
          return;
        }

        // Exchange the token for a session token
        String sessionToken;
        try {
          sessionToken =
            AuthSubUtil.exchangeForSessionToken(token,
                                                Utility.getPrivateKey());
        } catch (IOException e1) {
          resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                         "Exception retrieving session token.");
          return;
        } catch (GeneralSecurityException e1) {
          resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                         "Security error while retrieving session token.");
          return;
        } catch (AuthenticationException e) {
          resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                         "Server rejected one time use token.");
          return;
        }

        try {
          // Sanity checking usability of token
          Map<String, String> info =
            AuthSubUtil.getTokenInfo(sessionToken, Utility.getPrivateKey());
          for (Iterator<String> iter = info.keySet().iterator(); iter.hasNext();) {
            String key = iter.next();
            System.out.println("\t(key, value): (" + key + ", " + info.get(key)
                               + ")");
          }
        } catch (IOException e1) {
          resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                         "Exception retrieving info for session token.");
          return;
        } catch (GeneralSecurityException e1) {
          resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                         "Security error while retrieving session token info.");
          return;
        } catch (AuthenticationException e) {
          resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                         "Auth error retrieving info for session token: " +
                         e.getMessage());
          return;
        }

        // Retrieve the authentication cookie to identify user
        /*String principal = Utility.getCookieValueWithName(req.getCookies(), Utility.LOGIN_COOKIE_NAME);
        if (principal == null) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                         "Unidentified principal.");
          return;
        }*/
        //SecurityUtil.getUserID(false);

        Token tokenObject = new Token();
        //tokenObject.setUserID(SecurityUtil.getUserID());
        tokenObject.setTokenType(TokenStorage.GOOGLE_DOCS_TOKEN);
        tokenObject.setTokenValue(sessionToken);
        new TokenStorage().saveToken(tokenObject);

        // Redirect to main.jsp where the token will be used
        /*StringBuffer continueUrl = req.getRequestURL();
        int index = continueUrl.lastIndexOf("/");
        continueUrl.delete(index, continueUrl.length());
        continueUrl.append(LoginServlet.NEXT_URL);
        resp.sendRedirect(continueUrl.toString());*/
      }
}
