package com.easyinsight.html;

import com.easyinsight.dashboard.DashboardService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 9/3/13
 * Time: 1:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class DashboardFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        if (req instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) req;
            String path = request.getServletPath();
            Pattern p = Pattern.compile("^/html/dashboard/([a-zA-z0-9]+)/?.*$");
            Matcher m = p.matcher(path);
            String dashboardID = m.replaceAll("$1");
            request.setAttribute("public", new DashboardService().isDashboardPublic(dashboardID));
        }
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
