package com.easyinsight.api.v3;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.api.*;
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
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import sun.misc.BASE64Decoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 1/3/11
 * Time: 2:02 PM
 */
public abstract class APIServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authHeader = req.getHeader("Authorization");
        if (authHeader == null) {
            resp.setContentType("text/xml");
            resp.setStatus(401);
            resp.addHeader("WWW-Authenticate", "Basic realm=\"Easy Insight\"");
            resp.getOutputStream().write("<response><code>401</code><message>Your credentials were rejected.</message></response>".getBytes());
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

            resp.setContentType("text/xml");
            resp.setStatus(401);
            resp.getOutputStream().write("<response><code>401</code><message>Your credentials were rejected.</message></response>".getBytes());
            resp.getOutputStream().flush();
        } else {
            try {
                SecurityUtil.populateThreadLocal(userResponse.getUserName(), userResponse.getUserID(), userResponse.getAccountID(),
                        userResponse.getAccountType(), userResponse.isAccountAdmin(), userResponse.getFirstDayOfWeek(), userResponse.getPersonaName());
                EIConnection conn = Database.instance().getConnection();
                ResponseInfo responseInfo;
                try {
                    conn.setAutoCommit(false);
                    responseInfo = processXML(null, conn, req);
                    conn.commit();
                } catch (ServiceRuntimeException sre) {
                    conn.rollback();
                    LogClass.error(sre);
                    responseInfo = new ResponseInfo(ResponseInfo.BAD_REQUEST, "<message>" + sre.getMessage() + "</message>");
                } catch (ParsingException spe) {
                    conn.rollback();
                    responseInfo = new ResponseInfo(ResponseInfo.BAD_REQUEST, "<message>" + spe.getMessage() + "</message>");
                } catch (Exception e) {
                    conn.rollback();
                    LogClass.error(e);
                    responseInfo = new ResponseInfo(ResponseInfo.SERVER_ERROR, "<message>An internal error occurred on attempting to process the provided data. The error has been logged for our engineers to examine.</message>");
                } finally {
                    conn.setAutoCommit(true);
                    Database.closeConnection(conn);
                    SecurityUtil.clearThreadLocal();
                }
                resp.setContentType("text/xml");
                resp.setStatus(responseInfo.getCode());
                resp.getOutputStream().write(responseInfo.toResponse().getBytes());
                resp.getOutputStream().flush();
            } catch (Exception e) {
                resp.setContentType("text/xml");
                resp.setStatus(400);
                resp.getOutputStream().write("<response><code>400</code><message>Your XML was malformed.</message></response>".getBytes());
                resp.getOutputStream().flush();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authHeader = req.getHeader("Authorization");
        String headerValue = authHeader.split(" ")[1];
        BASE64Decoder decoder = new BASE64Decoder();
        String userPass = new String(decoder.decodeBuffer(headerValue));
        int p = userPass.indexOf(":");
        UserServiceResponse userResponse = null;
        String userID = "";
        if (p != -1) {
            userID = userPass.substring(0, p);
            String password = userPass.substring(p+1);
            try {
                userResponse = SecurityUtil.authenticateKeys(userID, password);
            } catch (com.easyinsight.security.SecurityException se) {
                userResponse = new UserService().authenticate(userID, password, false);
            }
        }

        if (userResponse == null || !userResponse.isSuccessful()) {
            String ipAddress  = req.getHeader("X-FORWARDED-FOR");
            if(ipAddress == null) {
                ipAddress = req.getRemoteAddr();
            }
            new UserService().logAuthentication(userID, userResponse == null ? null : userResponse.getUserID(), false, ipAddress, req.getHeader("User-Agent"));
            resp.setContentType("text/xml");
            resp.setStatus(401);
            resp.getOutputStream().write("<response><code>401</code><message>Your credentials were rejected.</message></response>".getBytes());
            resp.getOutputStream().flush();
        } else {
            try {
                SecurityUtil.populateThreadLocal(userResponse.getUserName(), userResponse.getUserID(), userResponse.getAccountID(),
                        userResponse.getAccountType(), userResponse.isAccountAdmin(), userResponse.getFirstDayOfWeek(), userResponse.getPersonaName());
                EIConnection conn = Database.instance().getConnection();
                ResponseInfo responseInfo;
                try {
                    conn.setAutoCommit(false);

                    Document doc = new Builder().build(req.getInputStream());
                    responseInfo = processXML(doc, conn, req);
                    conn.commit();
                } catch (ServiceRuntimeException sre) {
                    conn.rollback();
                    LogClass.error(sre);
                    responseInfo = new ResponseInfo(ResponseInfo.BAD_REQUEST, "<message>" + sre.getMessage() + "</message>");
                } catch (ParsingException spe) {
                    conn.rollback();
                    responseInfo = new ResponseInfo(ResponseInfo.BAD_REQUEST, "<message>" + spe.getMessage() + "</message>");
                } catch (Exception e) {
                    conn.rollback();
                    LogClass.error(e);
                    responseInfo = new ResponseInfo(ResponseInfo.SERVER_ERROR, "<message>An internal error occurred on attempting to process the provided data. The error has been logged for our engineers to examine.</message>");
                } finally {
                    conn.setAutoCommit(true);
                    Database.closeConnection(conn);
                    SecurityUtil.clearThreadLocal();
                }
                resp.setContentType("text/xml");
                resp.setStatus(responseInfo.getCode());
                resp.getOutputStream().write(responseInfo.toResponse().getBytes());
                resp.getOutputStream().flush();
            } catch (Exception e) {
                resp.setContentType("text/xml");
                resp.setStatus(400);
                resp.getOutputStream().write("<response><code>400</code><message>Your XML was malformed.</message></response>".getBytes());
                resp.getOutputStream().flush();
            }
        }
    }

    protected abstract ResponseInfo processXML(Document document, EIConnection conn, HttpServletRequest request) throws Exception;

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
        if (dataSourceIDs.size() == 0) {
            PreparedStatement accountQueryStmt = conn.prepareStatement("SELECT DISTINCT DATA_FEED.DATA_FEED_ID, DATA_FEED.UNCHECKED_API_ENABLED" +
                    " FROM UPLOAD_POLICY_USERS, DATA_FEED, user WHERE " +
                    "UPLOAD_POLICY_USERS.user_id = user.user_id AND user.account_id = ? AND DATA_FEED.ACCOUNT_VISIBLE = ? AND DATA_FEED.DATA_FEED_ID = UPLOAD_POLICY_USERS.FEED_ID AND (DATA_FEED.FEED_NAME = ? OR " +
                    "DATA_FEED.API_KEY = ?) AND DATA_FEED.VISIBLE = ?");
            accountQueryStmt.setLong(1, SecurityUtil.getAccountID());
            accountQueryStmt.setBoolean(2, true);
            accountQueryStmt.setString(3, dataSourceName);
            accountQueryStmt.setString(4, dataSourceName);
            accountQueryStmt.setBoolean(5, true);
            ResultSet accountRS = accountQueryStmt.executeQuery();
            while (accountRS.next()) {
                dataSourceIDs.put(accountRS.getLong(1), accountRS.getBoolean(2));
            }
        }
        return dataSourceIDs;
    }
}
