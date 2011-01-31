package com.easyinsight.api.v3;

import com.easyinsight.admin.HealthListener;
import com.easyinsight.admin.Status;
import com.easyinsight.config.ConfigLoader;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import com.xerox.amazonws.sqs2.Message;
import com.xerox.amazonws.sqs2.MessageQueue;
import com.xerox.amazonws.sqs2.SQSException;
import com.xerox.amazonws.sqs2.SQSUtils;
import nu.xom.Document;
import org.apache.jcs.JCS;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 1/26/11
 * Time: 10:51 AM
 */
public class HealthServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT SERVER_HOST FROM SERVER WHERE ENABLED = ?");
            queryStmt.setBoolean(1, true);
            ResultSet rs = queryStmt.executeQuery();
            String response = "<response><status>Success</status><message>All Good</message></response>";
            while (rs.next()) {
                String host = rs.getString(1);
                Status status = (Status) JCS.getInstance("servers").get(host);
                if (status == null) {
                    response = "<response><status>Failure</status><message>" + host + " has failed to update with any status information.</message></response>";
                    break;
                } else if ((System.currentTimeMillis() - status.getTime()) > (1000 * 60 * 2)) {
                    response = "<response><status>Failure</status><message>" + host + " hasn't updated updated status for at least two minutes.</message></response>";
                    break;
                } else if (HealthListener.FAILURE.equals(status.getCode())) {
                    response = "<response><status>Failure</status><message>" + status.getMessage() + "</message></response>";
                    break;
                }
            }
            resp.getOutputStream().write(response.getBytes());
            resp.getOutputStream().flush();
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }
}
