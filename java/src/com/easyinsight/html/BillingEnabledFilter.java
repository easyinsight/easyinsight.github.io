package com.easyinsight.html;

import com.easyinsight.config.ConfigLoader;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 11/29/12
 * Time: 8:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class BillingEnabledFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (!ConfigLoader.instance().isBillingEnabled()) {
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            httpServletResponse.sendRedirect(RedirectUtil.getURL((HttpServletRequest) servletRequest, "/app/billingDisabled.jsp"));
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    public void destroy() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
