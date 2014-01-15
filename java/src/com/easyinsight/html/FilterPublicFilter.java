package com.easyinsight.html;

import com.easyinsight.dashboard.DashboardService;
import com.easyinsight.logging.LogClass;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

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
            try {
                HttpServletRequest request = (HttpServletRequest) req;
                InputStream is = req.getInputStream();
                JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
                JSONObject filterObject = (JSONObject) parser.parse(is);
                Long filterID = Long.valueOf((Integer) filterObject.get("id"));
                request.setAttribute("public", Utils.isPublic(filterID));
                request.setAttribute("filterObject", filterObject);
            } catch(Exception e) {
                LogClass.error(e);
            }
        }
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
