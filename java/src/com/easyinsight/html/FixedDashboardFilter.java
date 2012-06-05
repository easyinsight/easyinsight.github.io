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
public class FixedDashboardFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpSession session = ((HttpServletRequest) servletRequest).getSession();
            Long fixedDashboardID = (Long) session.getAttribute("fixedDashboardID");
            if (fixedDashboardID != null && fixedDashboardID > 0) {
                HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
                httpServletResponse.sendRedirect("/html/fixedDashboard/" + fixedDashboardID);
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {
    }
}
