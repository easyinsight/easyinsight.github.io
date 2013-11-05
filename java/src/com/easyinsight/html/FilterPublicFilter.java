package com.easyinsight.html;

import com.easyinsight.dashboard.DashboardService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 11/4/13
 * Time: 11:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class FilterPublicFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        if (req instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) req;
            Long dashboardID = Long.parseLong(request.getParameter("filterID"));
            request.setAttribute("public", Utils.isPublic(dashboardID));
        }
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
