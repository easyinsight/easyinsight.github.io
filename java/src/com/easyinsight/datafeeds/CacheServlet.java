package com.easyinsight.datafeeds;

import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: jamesboe
 * Date: 5/5/14
 * Time: 10:30 AM
 */
public class CacheServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SecurityUtil.populateThreadLocalFromSession(req);
        try {
            String dataSourceID = req.getParameter("dataSourceID");
            CachedAddonDataSource.runUpdateFor(dataSourceID);
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            SecurityUtil.clearThreadLocal();
        }
    }
}
