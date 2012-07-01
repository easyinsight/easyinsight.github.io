package com.easyinsight.html;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.users.Account;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: jamesboe
 * Date: 5/24/12
 * Time: 9:26 AM
 */
public class AuthFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        boolean authorized = false;
        if (servletRequest instanceof HttpServletRequest) {
            HttpSession session = ((HttpServletRequest) servletRequest).getSession(false);
            if (session != null) {
                authorized = (session.getAttribute("accountID") != null);
            }
            if(!authorized) {
                session = ((HttpServletRequest) servletRequest).getSession(true);
                session.setAttribute("loginRedirect", ((HttpServletRequest) servletRequest).getRequestURI());
            } else {

            }
        }
        if (authorized) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            httpServletResponse.sendRedirect(RedirectUtil.getURL((HttpServletRequest) servletRequest, "/app/login.jsp"));
        }
    }

    public void destroy() {
    }
}
