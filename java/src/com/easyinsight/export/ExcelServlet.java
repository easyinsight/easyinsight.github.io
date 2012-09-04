package com.easyinsight.export;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * User: jamesboe
 * Date: Oct 28, 2010
 * Time: 11:22:26 AM
 */
public class ExcelServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long imageID = (Long) req.getSession().getAttribute("imageID");
        Long userID = (Long) req.getSession().getAttribute("userID");
        String anonID = (String) req.getSession().getAttribute("anonID");
        if (imageID != null && (userID != null || anonID != null)) {
            EIConnection conn = Database.instance().getConnection();
            try {
                PreparedStatement queryStmt;
                if (userID != null) {
                    queryStmt = conn.prepareStatement("SELECT EXCEL_FILE, REPORT_NAME FROM EXCEL_EXPORT WHERE EXCEL_EXPORT_ID = ? AND " +
                            "USER_ID = ?");
                    queryStmt.setLong(1, imageID);
                    queryStmt.setLong(2, userID);
                } else {
                    queryStmt = conn.prepareStatement("SELECT EXCEL_FILE, REPORT_NAME FROM EXCEL_EXPORT WHERE EXCEL_EXPORT_ID = ? AND " +
                            "ANONYMOUS_ID = ?");
                    queryStmt.setLong(1, imageID);
                    queryStmt.setString(2, anonID);
                }
                ResultSet rs = queryStmt.executeQuery();
                if (rs.next()) {
                    byte[] bytes = rs.getBytes(1);
                    String reportName = rs.getString(2);
                    resp.setContentType("application/excel");
                    resp.setContentLength(bytes.length);
                    reportName = URLEncoder.encode(reportName, "UTF-8");
                    //resp.setHeader("Content-disposition","inline; filename=" + reportName+".xls" );
                    resp.setHeader("Content-disposition","attachment; filename=" + reportName+".xls" );
                    resp.setHeader("Cache-Control","private"); //HTTP 1.1
                    resp.setHeader("Pragma","token"); //HTTP 1.0
                    resp.setDateHeader ("Expires", 0); //prevents caching at the proxy server
                    resp.getOutputStream().write(bytes);
                    resp.getOutputStream().flush();
                }
            } catch (Exception e) {
                LogClass.error(e);
            } finally {
                Database.closeConnection(conn);
            }
        } else {
            
        }
    }
}
