package com.easyinsight.email;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.PreparedStatement;

/**
 * User: jamesboe
 * Date: 10/9/12
 * Time: 3:33 PM
 */
public class SendGridEmailEventServlet extends HttpServlet {

    public static final int NO_RESULT = 0;
    public static final int SUCCESSFUL = 1;
    public static final int FAILED = 2;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String emailType = req.getParameter("emailType");
        String auditID = req.getParameter("auditID");
        String event = req.getParameter("event");
        System.out.println(req.getParameter("event") + " - " + req.getParameter("reason"));
        if ("ReportDelivery".equals(emailType)) {
            EIConnection conn = Database.instance().getConnection();
            try {
                PreparedStatement auditStmt = conn.prepareStatement("UPDATE REPORT_DELIVERY_AUDIT SET SUCCESSFUL = ?, MESSAGE = ? WHERE REPORT_DELIVERY_AUDIT_ID = ?");
                String message = null;
                int status = -1;
                if ("delivered".equals(event)) {
                    status = SUCCESSFUL;
                    message = "Successfully delivered";
                } else if ("dropped".equals(event)) {
                    status = FAILED;
                    message = req.getParameter("reason");
                } else if ("bounce".equals(event)) {
                    status = FAILED;
                    message = req.getParameter("reason");
                }
                if (status != -1) {
                    auditStmt.setInt(1, status);
                    auditStmt.setString(2, message);
                    auditStmt.setLong(3, Long.parseLong(auditID));
                    auditStmt.executeUpdate();
                }
                auditStmt.close();
            } catch (Exception e) {
                LogClass.error(e);
            } finally {
                Database.closeConnection(conn);
            }
        }
    }
}
