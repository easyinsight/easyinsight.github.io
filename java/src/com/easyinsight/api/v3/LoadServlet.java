package com.easyinsight.api.v3;

import com.easyinsight.analysis.AsyncReport;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.export.DeliveryScheduledTask;
import com.easyinsight.logging.LogClass;
import com.easyinsight.scheduler.DataSourceScheduledTask;
import com.easyinsight.scheduler.ScheduledTask;
import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

/**
 * User: jamesboe
 * Date: 1/26/11
 * Time: 10:51 AM
 */
@WebServlet(name="LoadAdmin",
        urlPatterns={"/loadAdmin"})
public class LoadServlet extends HttpServlet {

    class Server {
        String host;
        boolean reportListener;
        boolean dataSourceListener;
        boolean databaseListener;
        int assigned;
        int inProgress;
        boolean healthy;
        Date lastHealthyTime;
        List<Activity> currentActivities = new ArrayList<>();
    }

    class Activity {
        private String name;
        private long userID;
        private String userEmail;
        private String accountName;
        private String dataSourceName;
        private long taskID;

        Activity(String name, long userID, String userEmail, String accountName, String dataSourceName, long taskID) {
            this.name = name;
            this.userID = userID;
            this.userEmail = userEmail;
            this.accountName = accountName;
            this.dataSourceName = dataSourceName;
            this.taskID = taskID;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<Integer, Server> loadMap = new HashMap<>();
        int unassigned = 0;
        EIConnection conn = Database.instance().getConnection();
        try {


            PreparedStatement stmt = conn.prepareStatement("SELECT server_id, server_host, data_source_listener, database_listener, " +
                    "report_listener, healthy, last_healthy_time FROM " +
                    "server WHERE enabled = ?");
            stmt.setBoolean(1, true);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Server server = new Server();
                server.host = rs.getString(2);
                server.dataSourceListener = rs.getBoolean(3);
                server.databaseListener = rs.getBoolean(4);
                server.reportListener = rs.getBoolean(5);
                server.healthy = rs.getBoolean(6);
                server.lastHealthyTime = rs.getTimestamp(7);
                loadMap.put(rs.getInt(1), server);
            }
            stmt.close();
            PreparedStatement workStmt = conn.prepareStatement("SELECT async_report_request_id, assigned_server, request_state, user_id, request_type, " +
                    "data_source_id, cache_source_id, task_id FROM " +
                    "async_report_request WHERE request_state = ? OR request_state = ? OR request_state = ?");
            workStmt.setInt(1, AsyncReport.WAITING_ASSIGN);
            workStmt.setInt(2, AsyncReport.ASSIGNED);
            workStmt.setInt(3, AsyncReport.IN_PROGRESS);
            ResultSet workRS = workStmt.executeQuery();

            PreparedStatement userStmt = conn.prepareStatement("SELECT user.email, account.name FROM user, account where user_id = ? AND user.account_id = account.account_id");
            PreparedStatement dsStmt = conn.prepareStatement("SELECT data_feed.feed_name, data_feed.feed_type FROM data_feed WHERE data_feed_id = ?");



            List<Long> waitingForAssign = new ArrayList<>(1);
            while (workRS.next()) {
                long requestID = workRS.getLong(1);
                int assignedServer = workRS.getInt(2);

                boolean serverAssigned = !workRS.wasNull();
                int requestState = workRS.getInt(3);
                long userID = workRS.getLong(4);
                int requestType = workRS.getInt(5);
                long dataSourceID = workRS.getLong(6);
                long cacheID = workRS.getLong(7);
                long taskID = workRS.getLong(8);
                if (serverAssigned) {
                    Server server = loadMap.get(assignedServer);
                    if (server != null && requestState == AsyncReport.ASSIGNED) {
                        server.assigned++;
                    } else if (server != null && requestState == AsyncReport.IN_PROGRESS) {
                        server.inProgress++;
                    }
                    if (server != null) {
                        String activityName = null;
                        if (requestType == AsyncReport.DATA_SOURCE_REFRESH) {
                            activityName = "Data source refresh";
                        } else if (requestType == AsyncReport.REPORT_DATA_SET) {
                            activityName = "Report data set retrieval";
                        } else if (requestType == AsyncReport.REPORT_END_USER) {
                            activityName = "Report end user run";
                        } else if (requestType == AsyncReport.REPORT_EDITOR) {
                            activityName = "Report editor run";
                        } else if (requestType == AsyncReport.SCHEDULED_TASK) {
                            activityName = "Scheduled task";
                        } else if (requestType == AsyncReport.CACHE_REBUILD) {
                            activityName = "Cache rebuild";
                        } else if (requestType == AsyncReport.FILE_UPLOAD_ANALYZE) {
                            activityName = "Analyzing flat file create";
                        } else if (requestType == AsyncReport.FILE_UPLOAD_CREATE) {
                            activityName = "Creating source from flat file";
                        }  else if (requestType == AsyncReport.FILE_REPLACE_UPLOAD) {
                            activityName = "Analyzing flat file update";
                        } else if (requestType == AsyncReport.FILE_REPLACE_ANALYZE) {
                            activityName = "Updating source from flat file";
                        }
                        String email = null;
                        String name = null;
                        if (userID > 0) {
                            userStmt.setLong(1, userID);
                            ResultSet userRS = userStmt.executeQuery();
                            userRS.next();
                            email = userRS.getString(1);
                            name = userRS.getString(2);
                        }
                        String dataSourceName = null;
                        if (dataSourceID > 0) {
                            dsStmt.setLong(1, dataSourceID);
                            ResultSet dataSetRS = dsStmt.executeQuery();
                            dataSetRS.next();
                            dataSourceName = dataSetRS.getString(1);
                        }

                        server.currentActivities.add(new Activity(activityName, userID, email, name, dataSourceName, taskID));
                    }
                } else {
                    unassigned++;
                }
            }
            workStmt.close();
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }

        if (unassigned > 0) {
            resp.getOutputStream().write((unassigned + " unassigned requests waiting for processing...\r\n\r\n").getBytes());
        }

        for (Server server : loadMap.values()) {
            resp.getOutputStream().write((server.host + "\r\n").getBytes());
            resp.getOutputStream().write(("\tDatabase Listener: " + server.databaseListener + "\r\n").getBytes());
            resp.getOutputStream().write(("\tReport and Data Source Listener: " + server.reportListener + "\r\n").getBytes());
            resp.getOutputStream().write(("\tAssigned: " + server.assigned + "\r\n").getBytes());
            resp.getOutputStream().write(("\tIn Progress: " + server.inProgress + "\r\n").getBytes());
            resp.getOutputStream().write(("\tHealthy: " + server.healthy + "\r\n").getBytes());
            resp.getOutputStream().write(("\tLast Healthy Time: " + server.lastHealthyTime + "\r\n").getBytes());
            resp.getOutputStream().write(("\r\n").getBytes());

            if (server.currentActivities.size() > 0) {
                resp.getOutputStream().write(("\r\n\tCurrent Server Work:\r\n").getBytes());
                for (Activity activity : server.currentActivities) {
                    if (activity.taskID > 0) {
                        conn = Database.instance().getConnection();
                        Session session = Database.instance().createSession(conn);
                        try {
                            PreparedStatement dsStmt = conn.prepareStatement("SELECT data_feed.feed_name, data_feed.feed_type FROM data_feed WHERE data_feed_id = ?");
                            List tasks = session.createQuery("from ScheduledTask where scheduledTaskID = ?").setLong(0, activity.taskID).list();
                            if (tasks.size() > 0) {
                                ScheduledTask scheduledTask = (ScheduledTask) tasks.get(0);
                                if (scheduledTask instanceof DataSourceScheduledTask) {
                                    DataSourceScheduledTask dataSourceScheduledTask = (DataSourceScheduledTask) scheduledTask;
                                    long dataSourceID = dataSourceScheduledTask.getDataSourceID();
                                    dsStmt.setLong(1, dataSourceID);
                                    ResultSet rs = dsStmt.executeQuery();
                                    if (rs.next()) {
                                        String dataSourceName = rs.getString(1);
                                        resp.getOutputStream().write(("\t\tScheduled refresh of " + dataSourceName + "\r\n").getBytes());
                                    } else {
                                        resp.getOutputStream().write(("\t\tOrphan data source refresh\r\n").getBytes());
                                    }
                                } else {
                                    resp.getOutputStream().write(("\t\t" + scheduledTask.getClass().getName()).getBytes());
                                }
                            } else {
                                resp.getOutputStream().write(("\t\tOrphan task, shouldn't really be seeing this...").getBytes());
                            }
                            dsStmt.close();
                        } catch (Exception e) {
                            LogClass.error(e);
                        } finally {
                            session.close();
                            Database.closeConnection(conn);
                        }
                    } else if ("Data source refresh".equals(activity.name)) {
                        resp.getOutputStream().write(("\t\tRefresh of " + activity.dataSourceName + " by user " + activity.userEmail + " on " + activity.accountName + "\r\n").getBytes());
                    } else {
                        resp.getOutputStream().write(("\t\t" + activity.name + " by user " + activity.userEmail + " on " + activity.accountName + "\r\n").getBytes());
                    }
                }
            }
        }
        resp.getOutputStream().flush();
    }
}
