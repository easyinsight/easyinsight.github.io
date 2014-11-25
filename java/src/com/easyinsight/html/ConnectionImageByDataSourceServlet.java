package com.easyinsight.html;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.solutions.Solution;
import com.easyinsight.solutions.SolutionService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;

/**
 * User: jamesboe
 * Date: 5/24/12
 * Time: 2:50 PM
 */
public class ConnectionImageByDataSourceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SecurityUtil.populateThreadLocalFromSession(req);
        try {
            int dataSourceType = Integer.parseInt(req.getParameter("dataSourceType"));
            Date d = new Date();
            EIConnection conn = Database.instance().getConnection();
            try {
                Solution solution = new SolutionService().solutionForDataSourceType(dataSourceType, conn);
                if (solution != null) {
                    byte[] image = solution.getImage();
                    if (image != null) {
                        resp.setContentLength(image.length);
                        resp.setContentType("image/png");
                        SimpleDateFormat sdf = new SimpleDateFormat();
                        sdf.setTimeZone(new SimpleTimeZone(0, "GMT"));
                        sdf.applyPattern("dd MMM yyyy HH:mm:ss z");
                        resp.setHeader("Cache-control", "private, max-age: 2592000");
                        Calendar c = Calendar.getInstance();
                        c.add(Calendar.SECOND, 2592000);
                        resp.setHeader("Expires", sdf.format(c.getTime()));
                        resp.setHeader("Content-disposition", "inline; filename=reportHeader.png");
                        resp.getOutputStream().write(image);
                        resp.getOutputStream().flush();
                    }
                }
            } catch (Exception e) {
                LogClass.error(e);
            } finally {
                Database.closeConnection(conn);
            }
        } finally {
            SecurityUtil.clearThreadLocal();
        }
    }
}
