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
        String urlKey = req.getParameter("urlKey");
        if (urlKey != null) {
            EIConnection conn = Database.instance().getConnection();
            try {
                PreparedStatement queryStmt = conn.prepareStatement("SELECT EXCEL_FILE, REPORT_NAME, EXCEL_FORMAT FROM EXCEL_EXPORT WHERE URL_KEY = ?");
                queryStmt.setString(1, urlKey);
                ResultSet rs = queryStmt.executeQuery();
                if (rs.next()) {
                    byte[] bytes = rs.getBytes(1);
                    String reportName = rs.getString(2);
                    int format = rs.getInt(3);
                    String extension = format == 1 ? "xlsx" : "xls";
                    resp.setContentType("application/excel");
                    resp.setContentLength(bytes.length);
                    reportName = URLEncoder.encode(reportName, "UTF-8");
                    //resp.setHeader("Content-disposition","inline; filename=" + reportName+".xls" );
                    String userAgent = req.getHeader("User-Agent");
                    if (userAgent != null && userAgent.contains("MSIE 8.0")) {
                        resp.setHeader("Cache-Control","private"); //HTTP 1.1
                        resp.setHeader("Pragma","token"); //HTTP 1.0
                    } else {
                        resp.setHeader("Cache-Control","no-cache"); //HTTP 1.1
                        resp.setHeader("Pragma","no-cache"); //HTTP 1.0
                    }
                    resp.setHeader("Content-Disposition","attachment; filename=" + reportName+"."+extension );
                    resp.setDateHeader ("Expires", 0); //prevents caching at the proxy server
                    resp.getOutputStream().write(bytes);
                    resp.getOutputStream().flush();
                }
            } catch (Exception e) {
                LogClass.error(e);
            } finally {
                Database.closeConnection(conn);
            }
        }  else {
            Long imageID = (Long) req.getSession().getAttribute("imageID");
            Long userID = (Long) req.getSession().getAttribute("userID");
            String anonID = (String) req.getSession().getAttribute("anonID");
            if (imageID != null && (userID != null || anonID != null)) {
                EIConnection conn = Database.instance().getConnection();
                try {
                    PreparedStatement queryStmt;
                    if (userID != null) {
                        queryStmt = conn.prepareStatement("SELECT EXCEL_FILE, REPORT_NAME, EXCEL_FORMAT FROM EXCEL_EXPORT WHERE EXCEL_EXPORT_ID = ? AND " +
                                "USER_ID = ?");
                        queryStmt.setLong(1, imageID);
                        queryStmt.setLong(2, userID);
                    } else {
                        queryStmt = conn.prepareStatement("SELECT EXCEL_FILE, REPORT_NAME, EXCEL_FORMAT FROM EXCEL_EXPORT WHERE EXCEL_EXPORT_ID = ? AND " +
                                "ANONYMOUS_ID = ?");
                        queryStmt.setLong(1, imageID);
                        queryStmt.setString(2, anonID);
                    }
                    ResultSet rs = queryStmt.executeQuery();
                    if (rs.next()) {
                        byte[] bytes = rs.getBytes(1);
                        String reportName = rs.getString(2);
                        int format = rs.getInt(3);
                        String extension = format == 1 ? "xlsx" : "xls";
                        resp.setContentType("application/excel");
                        resp.setContentLength(bytes.length);
                        reportName = URLEncoder.encode(reportName, "UTF-8");
                        //resp.setHeader("Content-disposition","inline; filename=" + reportName+".xls" );
                        String userAgent = req.getHeader("User-Agent");
                        if (userAgent != null && userAgent.contains("MSIE 8.0")) {
                            resp.setHeader("Cache-Control","private"); //HTTP 1.1
                            resp.setHeader("Pragma","token"); //HTTP 1.0
                        } else {
                            resp.setHeader("Cache-Control","no-cache"); //HTTP 1.1
                            resp.setHeader("Pragma","no-cache"); //HTTP 1.0
                        }
                        resp.setHeader("Content-Disposition","attachment; filename=" + reportName+"."+extension );
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

    public static void main(String[] args) {
        /*
        var d3_category10 = [ 2062260, 16744206, 2924588, 14034728, 9725885, 9197131, 14907330, 8355711, 12369186, 1556175 ].map(d3_rgbString);
  var d3_category20 = [ 2062260, 11454440, 16744206, 16759672, 2924588, 10018698, 14034728, 16750742, 9725885, 12955861, 9197131, 12885140, 14907330, 16234194, 8355711, 13092807, 12369186, 14408589, 1556175, 10410725 ].map(d3_rgbString);
  var d3_category20b = [ 3750777, 5395619, 7040719, 10264286, 6519097, 9216594, 11915115, 13556636, 9202993, 12426809, 15186514, 15190932, 8666169, 11356490, 14049643, 15177372, 8077683, 10834324, 13528509, 14589654 ].map(d3_rgbString);
  var d3_category20c = [ 3244733, 7057110, 10406625, 13032431, 15095053, 16616764, 16625259, 16634018, 3253076, 7652470, 10607003, 13101504, 7695281, 10394312, 12369372, 14342891, 6513507, 9868950, 12434877, 14277081 ].map(d3_rgbString);
         */
        System.out.println(String.format("#%06X", (0xFFFFFF & 16744206)));
        System.out.println(String.format("#%06X", (0xFFFFFF & 16759672)));
        System.out.println(String.format("#%06X", (0xFFFFFF & 2924588)));
        System.out.println(String.format("#%06X", (0xFFFFFF & 10018698)));
        System.out.println(String.format("#%06X", (0xFFFFFF & 14034728)));
        System.out.println(String.format("#%06X", (0xFFFFFF & 16750742)));
        System.out.println(String.format("#%06X", (0xFFFFFF & 9725885)));
        System.out.println(String.format("#%06X", (0xFFFFFF & 12955861)));
        System.out.println(String.format("#%06X", (0xFFFFFF & 9197131)));
        System.out.println(String.format("#%06X", (0xFFFFFF & 12885140)));
        System.out.println(String.format("#%06X", (0xFFFFFF & 14907330)));
        System.out.println(String.format("#%06X", (0xFFFFFF & 16234194)));
    }
}
