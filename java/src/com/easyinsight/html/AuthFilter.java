package com.easyinsight.html;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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
        }
        if (authorized) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            httpServletResponse.sendRedirect("/app/login.jsp");
        }
    }

    public void destroy() {
    }
}
