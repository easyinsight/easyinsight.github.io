package com.easyinsight.api.v3;

import com.easyinsight.admin.HealthListener;
import com.easyinsight.admin.Status;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import org.apache.jcs.JCS;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
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
            List<Status> statusList = new ArrayList<Status>();
            while (rs.next()) {
                String host = rs.getString(1);
                Status status = (Status) JCS.getInstance("servers").get(host);

                if (status == null) {
                    status = new Status();
                    status.setExtendedCode("Failure");
                    status.setExtendedMessage(host + " has failed to update with any status information");
                    statusList.add(status);
                } else if ((System.currentTimeMillis() - status.getTime()) > (1000 * 60 * 2)) {
                    status.setExtendedCode("Failure");
                    status.setExtendedMessage(host + " hasn't updated updated status for at least two minutes");
                    statusList.add(status);
                } else if (HealthListener.FAILURE.equals(status.getCode())) {
                    status.setExtendedCode("Failure");
                    status.setExtendedMessage(status.getMessage());
                    statusList.add(status);
                } else {
                    statusList.add(status);
                }
            }
            StringBuilder xmlBuilder = new StringBuilder();
            xmlBuilder.append("<response>\r\n");
            for (Status status : statusList) {
                xmlBuilder.append("\t<server>\r\n");
                xmlBuilder.append("\t\t<status>");
                xmlBuilder.append(status.getExtendedCode());
                xmlBuilder.append("</status>\r\n");
                xmlBuilder.append(status.getHealthInfo() == null ? "" : status.getHealthInfo().toXML());
                xmlBuilder.append("\t\t<message>");
                xmlBuilder.append(status.getExtendedMessage());
                xmlBuilder.append("</message>\r\n");
                xmlBuilder.append("\t\t<time>");
                xmlBuilder.append(new Date(status.getTime()));
                xmlBuilder.append("</time>\r\n");
                xmlBuilder.append("\t</server>\r\n");
            }
            xmlBuilder.append("</response>");
            resp.getOutputStream().write(xmlBuilder.toString().getBytes());
            resp.getOutputStream().flush();
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }
}
