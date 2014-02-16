package com.easyinsight.datafeeds.custom;

import com.easyinsight.cache.MemCachedManager;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.AccountAdminTO;
import com.easyinsight.users.EIAccountManagementService;
import com.easyinsight.users.UserService;
import com.easyinsight.users.UserServiceResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import sun.misc.BASE64Decoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 2/9/12
 * Time: 1:20 PM
 */
public class ThreadDumpServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EIConnection conn = Database.instance().getConnection();
        try {
            String authHeader = req.getHeader("Authorization");
            if (authHeader == null) {
                resp.setContentType("application/json");
                resp.setStatus(401);
                resp.addHeader("WWW-Authenticate", "Basic realm=\"Easy Insight\"");
                resp.getOutputStream().write("Your credentials were rejected.".getBytes());
                resp.getOutputStream().flush();
                return;
            }
            String headerValue = authHeader.split(" ")[1];
            BASE64Decoder decoder = new BASE64Decoder();
            String userPass = new String(decoder.decodeBuffer(headerValue));
            int p = userPass.indexOf(":");
            UserServiceResponse userResponse = null;
            if (p != -1) {
                String userID = userPass.substring(0, p);
                String password = userPass.substring(p+1);
                try {
                    userResponse = SecurityUtil.authenticateKeys(userID, password);
                } catch (com.easyinsight.security.SecurityException se) {
                    userResponse = new UserService().authenticate(userID, password, false);
                }
            }

            if (userResponse == null || !userResponse.isSuccessful()) {
                resp.setContentType("application/json");
                resp.setStatus(401);
                resp.getOutputStream().write("Your credentials were rejected.".getBytes());
                resp.getOutputStream().flush();
            } else {

                boolean eiOnly = req.getParameter("ei") != null;

                PreparedStatement queryStmt = conn.prepareStatement("SELECT SERVER_HOST FROM SERVER WHERE ENABLED = ?");
                queryStmt.setBoolean(1, true);
                ResultSet rs = queryStmt.executeQuery();
                Set<String> hosts = new HashSet<String>();
                while (rs.next()) {
                    hosts.add(rs.getString(1));
                }

                MemCachedManager.instance().add("threadDump", 10000, "getThreads");

                String localhost = InetAddress.getLocalHost().getHostName();
                hosts.remove(localhost);
                Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();

                Map<Long, StackTraceElement[]> copy = new HashMap<Long, StackTraceElement[]>();
                for (Map.Entry<Thread, StackTraceElement[]> entry : map.entrySet()) {
                    copy.put(entry.getKey().getId(), entry.getValue());
                }
                Map<String, Map<Long, StackTraceElement[]>> stackMap = new HashMap<String, Map<Long, StackTraceElement[]>>();
                stackMap.put(localhost, copy);
                int retryCount = 0;
                while (!hosts.isEmpty() && retryCount < 10) {
                    Iterator<String> hostIter = hosts.iterator();
                    while (hostIter.hasNext()) {
                        String host = hostIter.next();
                        Object obj = MemCachedManager.instance().get("threadDump" + host);
                        if (obj != null) {
                            Map<Long, StackTraceElement[]> hostMap = (Map<Long, StackTraceElement[]>) obj;
                            hostIter.remove();
                            MemCachedManager.instance().delete("threadDump" + host);
                            stackMap.put(host, hostMap);
                        }
                    }
                    if (!hosts.isEmpty()) {
                        Thread.sleep(1000);
                        retryCount++;
                    }
                }

                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, Map<Long, StackTraceElement[]>> entry : stackMap.entrySet()) {
                    String host = entry.getKey();
                    Map<Long, StackTraceElement[]> stacks = entry.getValue();
                    sb.append(host);
                    sb.append("\r\n\r\n");
                    for (Map.Entry<Long, StackTraceElement[]> stackEntry : stacks.entrySet()) {
                        if (eiOnly) {
                            boolean found = false;
                            for (StackTraceElement stackTraceElement : stackEntry.getValue()) {
                                if (stackTraceElement.getClassName().contains("easyinsight")) {
                                    found = true;
                                    break;
                                }
                            }
                            if (found) {
                                sb.append("Thread ").append(stackEntry.getKey()).append("\r\n\r\n");
                                for (StackTraceElement stackTraceElement : stackEntry.getValue()) {
                                    sb.append(stackTraceElement.toString()).append("\r\n");
                                }
                                sb.append("\r\n");
                            }
                        } else {
                            sb.append("Thread ").append(stackEntry.getKey()).append("\r\n\r\n");
                            for (StackTraceElement stackTraceElement : stackEntry.getValue()) {
                                sb.append(stackTraceElement.toString()).append("\r\n");
                            }
                            sb.append("\r\n");
                        }

                    }
                }


                    resp.setContentType("application/json");
                    resp.setStatus(200);
                    resp.getOutputStream().write(sb.toString().getBytes());
                    resp.getOutputStream().flush();

            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }
}
