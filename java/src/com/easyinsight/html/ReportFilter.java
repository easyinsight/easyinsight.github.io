package com.easyinsight.html;

import com.easyinsight.analysis.AnalysisService;
import com.easyinsight.dashboard.DashboardService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 11/4/13
 * Time: 11:49 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReportFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        if (req instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) req;
            String path = request.getServletPath();
            Pattern p = Pattern.compile("^/html/report/([a-zA-z0-9]+)/?.*$");
            Matcher m = p.matcher(path);
            String reportID = m.replaceAll("$1");
            request.setAttribute("public", new AnalysisService().isReportPublic(reportID));
        }
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}