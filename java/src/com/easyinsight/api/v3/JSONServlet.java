package com.easyinsight.api.v3;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.api.DataTransformation;
import com.easyinsight.api.ServiceRuntimeException;
import com.easyinsight.benchmark.BenchmarkManager;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.users.UserService;
import com.easyinsight.users.UserServiceResponse;
import com.easyinsight.userupload.UploadPolicy;
import net.minidev.json.JSONArray;
import net.minidev.json.parser.JSONParser;
import nu.xom.ParsingException;
import org.json.JSONException;
import org.json.JSONObject;
import sun.misc.BASE64Decoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 1/3/11
 * Time: 2:02 PM
 */
public abstract class JSONServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authHeader = req.getHeader("Authorization");
        if (authHeader == null) {
            resp.addHeader("WWW-Authenticate", "Basic realm=\"Easy Insight\"");
            sendError(401, "Your credentials were rejected.", resp);
            return;
        }
        String headerValue = authHeader.split(" ")[1];
        BASE64Decoder decoder = new BASE64Decoder();
        String userPass = new String(decoder.decodeBuffer(headerValue));
        int p = userPass.indexOf(":");
        UserServiceResponse userResponse = null;
        if (p != -1) {
            String userID = userPass.substring(0, p);
            String password = userPass.substring(p + 1);
            try {
                userResponse = SecurityUtil.authenticateKeys(userID, password);
            } catch (com.easyinsight.security.SecurityException se) {
                userResponse = new UserService().authenticate(userID, password, false);
            }
        }
        Date start = new Date();
        if (userResponse == null || !userResponse.isSuccessful()) {
            sendError(401, "Your credentials were rejected.", resp);
        } else {
            try {
                SecurityUtil.populateThreadLocal(userResponse.getUserName(), userResponse.getUserID(), userResponse.getAccountID(),
                        userResponse.getAccountType(), userResponse.isAccountAdmin(), userResponse.getFirstDayOfWeek(), userResponse.getPersonaName());
                EIConnection conn = Database.instance().getConnection();
                ResponseInfo responseInfo;
                try {
                    conn.setAutoCommit(false);
                    responseInfo = processJSON(null, conn, req);
                    conn.commit();
                } catch (ServiceRuntimeException sre) {
                    conn.rollback();
                    LogClass.error(sre);
                    responseInfo = new ResponseInfo(ResponseInfo.BAD_REQUEST, sre.getMessage());
                } catch (ParsingException spe) {
                    conn.rollback();
                    responseInfo = new ResponseInfo(ResponseInfo.BAD_REQUEST, spe.getMessage());
                } catch (Exception e) {
                    conn.rollback();
                    LogClass.error(e);
                    responseInfo = new ResponseInfo(ResponseInfo.SERVER_ERROR, "An internal error occurred on attempting to process the provided data. The error has been logged for our engineers to examine.");
                } finally {
                    conn.setAutoCommit(true);
                    Database.closeConnection(conn);
                    SecurityUtil.clearThreadLocal();
                }
                resp.setContentType("application/json");
                resp.setStatus(responseInfo.getCode());
                resp.getOutputStream().write(responseInfo.getXml().getBytes());
                resp.getOutputStream().flush();
            } catch (Exception e) {
                sendError(400, "Your request was malformed.", resp);
            }
        }
        Date end = new Date();
        BenchmarkManager.recordBenchmark(this.getClass().getCanonicalName(), (end.getTime() - start.getTime()), userResponse.getUserID());
        System.out.println("API Call: " + this.getClass().getCanonicalName() + " Duration: " + (end.getTime() - start.getTime()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authHeader = req.getHeader("Authorization");
        if (authHeader == null) {
            resp.addHeader("WWW-Authenticate", "Basic realm=\"Easy Insight\"");
            sendError(401, "Your credentials were rejected.", resp);
            return;
        }
        String headerValue = authHeader.split(" ")[1];
        BASE64Decoder decoder = new BASE64Decoder();
        String userPass = new String(decoder.decodeBuffer(headerValue));
        int p = userPass.indexOf(":");
        UserServiceResponse userResponse = null;
        if (p != -1) {
            String userID = userPass.substring(0, p);
            String password = userPass.substring(p + 1);
            try {
                userResponse = SecurityUtil.authenticateKeys(userID, password);
            } catch (com.easyinsight.security.SecurityException se) {
                userResponse = new UserService().authenticate(userID, password, false);
            }
        }

        Date start = new Date();
        if (userResponse == null || !userResponse.isSuccessful()) {
            sendError(401, "Your credentials were rejected.", resp);
        } else {
            try {
                SecurityUtil.populateThreadLocal(userResponse.getUserName(), userResponse.getUserID(), userResponse.getAccountID(),
                        userResponse.getAccountType(), userResponse.isAccountAdmin(), userResponse.getFirstDayOfWeek(), userResponse.getPersonaName());

                InputStream is = req.getInputStream();
                JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
                Object o = parser.parse(is);
                net.minidev.json.JSONObject postObject;
                if (o instanceof JSONArray) {
                    postObject = new net.minidev.json.JSONObject();
                    postObject.put("rows", o);
                } else {
                    postObject = (net.minidev.json.JSONObject) o;
                }
                EIConnection conn = Database.instance().getConnection();
                ResponseInfo responseInfo;
                try {
                    conn.setAutoCommit(false);
                    responseInfo = processJSON(postObject, conn, req);
                    conn.commit();
                } catch (ServiceRuntimeException sre) {
                    conn.rollback();
                    LogClass.error(sre);
                    responseInfo = new ResponseInfo(ResponseInfo.BAD_REQUEST, sre.getMessage());
                } catch (ParsingException spe) {
                    conn.rollback();
                    responseInfo = new ResponseInfo(ResponseInfo.BAD_REQUEST, spe.getMessage());
                } catch (Exception e) {
                    conn.rollback();
                    LogClass.error(e);
                    responseInfo = new ResponseInfo(ResponseInfo.SERVER_ERROR, "An internal error occurred on attempting to process the provided data. The error has been logged for our engineers to examine.");
                } finally {
                    conn.setAutoCommit(true);
                    Database.closeConnection(conn);
                    SecurityUtil.clearThreadLocal();
                }
                resp.setContentType("application/json");
                resp.setStatus(responseInfo.getCode());
                resp.getOutputStream().write(responseInfo.getXml().getBytes());
                resp.getOutputStream().flush();
            } catch (Exception e) {
                sendError(400, "Your request was malformed.", resp);
            }
        }
        Date end = new Date();
        BenchmarkManager.recordBenchmark(this.getClass().getCanonicalName(), (end.getTime() - start.getTime()), userResponse.getUserID());
        System.out.println("API Call: " + this.getClass().getCanonicalName() + " Duration: " + (end.getTime() - start.getTime()));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authHeader = req.getHeader("Authorization");
        if (authHeader == null) {
            resp.addHeader("WWW-Authenticate", "Basic realm=\"Easy Insight\"");
            sendError(401, "Your credentials were rejected.", resp);
            return;
        }
        String headerValue = authHeader.split(" ")[1];
        BASE64Decoder decoder = new BASE64Decoder();
        String userPass = new String(decoder.decodeBuffer(headerValue));
        int p = userPass.indexOf(":");
        UserServiceResponse userResponse = null;
        if (p != -1) {
            String userID = userPass.substring(0, p);
            String password = userPass.substring(p + 1);
            try {
                userResponse = SecurityUtil.authenticateKeys(userID, password);
            } catch (com.easyinsight.security.SecurityException se) {
                userResponse = new UserService().authenticate(userID, password, false);
            }
        }

        Date start = new Date();
        if (userResponse == null || !userResponse.isSuccessful()) {
            sendError(401, "Your credentials were rejected.", resp);
        } else {
            try {
                SecurityUtil.populateThreadLocal(userResponse.getUserName(), userResponse.getUserID(), userResponse.getAccountID(),
                        userResponse.getAccountType(), userResponse.isAccountAdmin(), userResponse.getFirstDayOfWeek(), userResponse.getPersonaName());

                InputStream is = req.getInputStream();
                JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
                Object o = parser.parse(is);
                net.minidev.json.JSONObject postObject;
                if (o instanceof JSONArray) {
                    postObject = new net.minidev.json.JSONObject();
                    postObject.put("rows", o);
                } else {
                    postObject = (net.minidev.json.JSONObject) o;
                }
                EIConnection conn = Database.instance().getConnection();
                ResponseInfo responseInfo;
                try {
                    conn.setAutoCommit(false);
                    responseInfo = processJSON(postObject, conn, req);
                    conn.commit();
                } catch (ServiceRuntimeException sre) {
                    conn.rollback();
                    LogClass.error(sre);
                    responseInfo = new ResponseInfo(ResponseInfo.BAD_REQUEST, sre.getMessage());
                } catch (ParsingException spe) {
                    conn.rollback();
                    responseInfo = new ResponseInfo(ResponseInfo.BAD_REQUEST, spe.getMessage());
                } catch (Exception e) {
                    conn.rollback();
                    LogClass.error(e);
                    responseInfo = new ResponseInfo(ResponseInfo.SERVER_ERROR, "An internal error occurred on attempting to process the provided data. The error has been logged for our engineers to examine.");
                } finally {
                    conn.setAutoCommit(true);
                    Database.closeConnection(conn);
                    SecurityUtil.clearThreadLocal();
                }
                resp.setContentType("application/json");
                resp.setStatus(responseInfo.getCode());
                resp.getOutputStream().write(responseInfo.getXml().getBytes());
                resp.getOutputStream().flush();
            } catch (Exception e) {
                sendError(400, "Your request was malformed.", resp);
            }
        }
        Date end = new Date();
        BenchmarkManager.recordBenchmark(this.getClass().getCanonicalName(), (end.getTime() - start.getTime()), userResponse.getUserID());
        System.out.println("API Call: " + this.getClass().getCanonicalName() + " Duration: " + (end.getTime() - start.getTime()));
    }

    protected abstract ResponseInfo processJSON(net.minidev.json.JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception;

    protected void sendError(int status, String error, HttpServletResponse resp) throws IOException {
        try {
            resp.setContentType("application/json");
            resp.setStatus(status);
            JSONObject jo = new JSONObject();
            jo.put("message", error);
            resp.getOutputStream().write(jo.toString().getBytes());
            resp.getOutputStream().flush();
        } catch (JSONException e) {
            LogClass.error(e);
        }
    }

    protected static class CallData {
        DataStorage dataStorage;
        String apiKey;

        private CallData(DataStorage dataStorage, String apiKey) {
            this.dataStorage = dataStorage;
            this.apiKey = apiKey;
        }
    }

    protected CallData convertData(String dataSourceName, List<AnalysisItem> analysisItems, EIConnection conn, boolean updateIfNecessary) throws Exception {
        if (dataSourceName == null) {
            throw new ServiceRuntimeException("You must specify a data source name or API key.");
        }
        DataStorage dataStorage;
        DataSet dataSet;
        Map<Long, Boolean> dataSourceIDs = findDataSourceIDsByName(dataSourceName, conn);
        String apiKey;
        if (updateIfNecessary) {
            if (dataSourceIDs.size() == 0) {
                long userID = SecurityUtil.getUserID();
                // create new data source
                FeedDefinition feedDefinition = new FeedDefinition();
                if (dataSourceName.length() < 3) {
                    throw new ServiceRuntimeException("The data source name must be at least three characters.");
                }
                if (dataSourceName.length() > 30) {
                    throw new ServiceRuntimeException("The data source name must be less than thirty characters.");
                }
                feedDefinition.setFeedName(dataSourceName);
                feedDefinition.setUncheckedAPIEnabled(true);
                feedDefinition.setAccountVisible(true);
                UploadPolicy uploadPolicy = new UploadPolicy(userID, SecurityUtil.getAccountID());
                feedDefinition.setUploadPolicy(uploadPolicy);
                feedDefinition.setFields(analysisItems);
                FeedCreationResult result = new FeedCreation().createFeed(feedDefinition, conn, new DataSet(), uploadPolicy);
                apiKey = feedDefinition.getApiKey();
                dataStorage = result.getTableDefinitionMetadata();
            } else if (dataSourceIDs.size() > 1) {
                throw new ServiceRuntimeException("More than one data source was found by that name. Please specify an API key for the data source instead.");
            } else {
                Map.Entry<Long, Boolean> entry = dataSourceIDs.entrySet().iterator().next();
                if (!entry.getValue()) {
                    throw new ServiceRuntimeException("This data source has been set to prohibit use of changeDataSourceToMatch as true.");
                }
                FeedStorage feedStorage = new FeedStorage();
                FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(entry.getKey());
                boolean newFieldsFound = false;
                List<AnalysisItem> previousItems = feedDefinition.getFields();
                for (AnalysisItem newItem : analysisItems) {
                    boolean newKey = true;
                    for (AnalysisItem previousItem : previousItems) {
                        if (newItem.getKey().equals(previousItem.getKey()) && newItem.getType() == previousItem.getType()) {
                            // matched the item...
                            newItem.setKey(previousItem.getKey());
                            newKey = false;
                        }
                    }
                    if (newKey) {
                        newFieldsFound = true;
                    }
                }
                if (newFieldsFound) {
                    feedDefinition.setFields(analysisItems);
                    feedStorage.updateDataFeedConfiguration(feedDefinition, conn);
                    dataStorage = DataStorage.writeConnection(feedDefinition, conn, SecurityUtil.getAccountID());
                    dataStorage.migrate(previousItems, analysisItems, false);
                    new DataSourceInternalService().updateComposites(feedDefinition, conn);
                } else {
                    dataStorage = DataStorage.writeConnection(feedDefinition, conn, SecurityUtil.getAccountID());
                }
                apiKey = feedDefinition.getApiKey();
            }
        } else {
            if (dataSourceIDs.size() == 0) {
                throw new ServiceRuntimeException("No data source was found by that name or API key.");
            } else if (dataSourceIDs.size() > 1) {
                throw new ServiceRuntimeException("More than one data source was found by that name. Please specify an API key for the data source instead.");
            } else {
                FeedStorage feedStorage = new FeedStorage();
                FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(dataSourceIDs.keySet().iterator().next(), conn);
                DataTransformation dataTransformation = new DataTransformation(feedDefinition);
                dataStorage = DataStorage.writeConnection(feedDefinition, conn, SecurityUtil.getAccountID());
                apiKey = feedDefinition.getApiKey();
            }
        }
        return new CallData(dataStorage, apiKey);
    }

    protected Map<Long, Boolean> findDataSourceIDsByName(String dataSourceName, Connection conn) throws SQLException {
        Map<Long, Boolean> dataSourceIDs = new HashMap<Long, Boolean>();
        PreparedStatement queryStmt = conn.prepareStatement("SELECT DISTINCT DATA_FEED.DATA_FEED_ID, DATA_FEED.UNCHECKED_API_ENABLED" +
                " FROM UPLOAD_POLICY_USERS, DATA_FEED, user WHERE " +
                "UPLOAD_POLICY_USERS.user_id = user.user_id AND user.user_id = ? AND DATA_FEED.DATA_FEED_ID = UPLOAD_POLICY_USERS.FEED_ID AND (DATA_FEED.FEED_NAME = ? OR " +
                "DATA_FEED.API_KEY = ?) AND DATA_FEED.VISIBLE = ?");
        queryStmt.setLong(1, SecurityUtil.getUserID());
        queryStmt.setString(2, dataSourceName);
        queryStmt.setString(3, dataSourceName);
        queryStmt.setBoolean(4, true);
        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            dataSourceIDs.put(rs.getLong(1), rs.getBoolean(2));
        }
        return dataSourceIDs;
    }
}
