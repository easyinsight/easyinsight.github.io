package com.easyinsight.api;

import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;
import com.easyinsight.api.dynamic.DynamicServiceDefinition;
import com.easyinsight.api.dynamic.ConfiguredMethod;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedDescriptor;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.Roles;
import com.easyinsight.AnalysisItem;

import java.util.List;
import java.util.ArrayList;
import java.sql.*;

import org.hibernate.Session;

/**
 * User: James Boe
 * Date: Sep 1, 2008
 * Time: 6:27:22 PM
 */
public class APIService {
    public void enableAPI(long feedID) {

    }

    public List<FeedDescriptor> getAvailableFeeds() {
        long userID = SecurityUtil.getUserID();
        List<FeedDescriptor> descriptors = new ArrayList<FeedDescriptor>();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_FEED.DATA_FEED_ID, FEED_NAME FROM DATA_FEED, UPLOAD_POLICY_USERS " +
                    "WHERE DYNAMIC_SERVICE_DEFINITION_ID IS NULL AND UPLOAD_POLICY_USERS.FEED_ID = DATA_FEED.DATA_FEED_ID AND " +
                    "UPLOAD_POLICY_USERS.USER_ID = ? AND UPLOAD_POLICY_USERS.ROLE = ?");
            queryStmt.setLong(1, userID);
            queryStmt.setInt(2, Roles.OWNER);
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                long feedID = rs.getLong(1);
                String feedName = rs.getString(2);
                FeedDescriptor feedDescriptor = new FeedDescriptor();
                feedDescriptor.setDataFeedID(feedID);
                feedDescriptor.setName(feedName);
                descriptors.add(feedDescriptor);
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        return descriptors;
    }

    public List<DynamicServiceDescriptor> getDeployedServices() {
        long userID = SecurityUtil.getUserID();
        List<DynamicServiceDescriptor> dynamicServiceDescriptors = new ArrayList<DynamicServiceDescriptor>();
        Connection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);        
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DYNAMIC_SERVICE_DESCRIPTOR.DYNAMIC_SERVICE_DESCRIPTOR_ID," +
                    "DYNAMIC_SERVICE_DESCRIPTOR.FEED_ID, DATA_FEED.FEED_NAME FROM DYNAMIC_SERVICE_DESCRIPTOR, " +
                    "DATA_FEED, UPLOAD_POLICY_USERS WHERE DYNAMIC_SERVICE_DESCRIPTOR.FEED_ID = DATA_FEED.DATA_FEED_ID AND " +
                    "UPLOAD_POLICY_USERS.FEED_ID = DATA_FEED.DATA_FEED_ID AND UPLOAD_POLICY_USERS.USER_ID = ? AND UPLOAD_POLICY_USERS.ROLE = ?");
            queryStmt.setLong(1, userID);
            queryStmt.setInt(2, Roles.OWNER);
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                long serviceID = rs.getLong(1);
                long feedID = rs.getLong(2);
                String feedName = rs.getString(3);
                String wsdl = "http://localhost:8080/DMS/services/s" + feedID + "?wsdl";
                dynamicServiceDescriptors.add(new DynamicServiceDescriptor(feedID, feedName, wsdl, serviceID, getDynamicServiceDefinition(feedID, conn, session)));
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            session.close();
            Database.instance().closeConnection(conn);
        }
        return dynamicServiceDescriptors;
    }

    public DynamicServiceDefinition getDynamicServiceDefinition(long feedID, Connection conn, Session session) {
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DYNAMIC_SERVICE_DESCRIPTOR_ID FROM " +
                    "DYNAMIC_SERVICE_DESCRIPTOR WHERE FEED_ID = ?");
            queryStmt.setLong(1, feedID);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                long serviceID = rs.getLong(1);
                PreparedStatement queryMethodStmt = conn.prepareStatement("SELECT DYNAMIC_SERVICE_METHOD_ID, METHOD_NAME, METHOD_TYPE FROM " +
                        "DYNAMIC_SERVICE_METHOD WHERE DYNAMIC_SERVICE_DESCRIPTOR_ID = ?");
                queryMethodStmt.setLong(1, serviceID);
                List<ConfiguredMethod> methods = new ArrayList<ConfiguredMethod>();
                ResultSet methodRS = queryMethodStmt.executeQuery();
                while (methodRS.next()) {
                    long methodID = methodRS.getLong(1);
                    String methodName = methodRS.getString(2);
                    int methodType = methodRS.getInt(3);
                    PreparedStatement queryKeysStmt = conn.prepareStatement("SELECT ANALYSIS_ITEM_ID FROM DYNAMIC_SERVICE_METHOD_KEY WHERE " +
                            "DYNAMIC_SERVICE_METHOD_ID = ?");
                    queryKeysStmt.setLong(1, methodID);
                    List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
                    ResultSet keyRS = queryKeysStmt.executeQuery();
                    while (keyRS.next()) {
                        long analysisItemID = keyRS.getLong(1);
                        List items = session.createQuery("from AnalysisItem where analysisItemID = ?").setLong(0, analysisItemID).list();
                        AnalysisItem analysisItem = (AnalysisItem) items.get(0);
                        analysisItems.add(analysisItem);
                    }
                    methods.add(new ConfiguredMethod(methodType, methodName, analysisItems));
                }
                DynamicServiceDefinition def = new DynamicServiceDefinition(feedID, serviceID);
                def.setConfiguredMethods(methods);
                return def;
            } else {
                return null;
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public DynamicServiceDefinition getDynamicServiceDefinition(long feedID) {
        Connection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            return getDynamicServiceDefinition(feedID, conn, session);
        } finally {
            session.close();
            Database.instance().closeConnection(conn);
        }
    }

    private void addDynamicServiceDefinition(DynamicServiceDefinition definition) {
        Connection conn = Database.instance().getConnection();
        try {
            addDynamicServiceDefinition(definition, conn);
        } finally {
            Database.instance().closeConnection(conn);
        }
    }

    public void addDynamicServiceDefinition(DynamicServiceDefinition definition, Connection conn) {

        try {
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO DYNAMIC_SERVICE_DESCRIPTOR (FEED_ID) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS);
            insertStmt.setLong(1, definition.getFeedID());
            insertStmt.execute();
            long id = Database.instance().getAutoGenKey(insertStmt);
            definition.setServiceID(id);
            PreparedStatement insertMethodStmt = conn.prepareStatement("INSERT INTO DYNAMIC_SERVICE_METHOD (DYNAMIC_SERVICE_DESCRIPTOR_ID," +
                    "method_name, method_type) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            PreparedStatement insertItemStmt = conn.prepareStatement("INSERT INTO DYNAMIC_SERVICE_METHOD_KEY (DYNAMIC_SERVICE_METHOD_ID," +
                    "ANALYSIS_ITEM_ID) VALUES (?, ?)");
            for (ConfiguredMethod configuredMethod : definition.getConfiguredMethods()) {
                insertMethodStmt.setLong(1, id);
                insertMethodStmt.setString(2, configuredMethod.getMethodName());
                insertMethodStmt.setInt(3, configuredMethod.getMethodType());
                insertMethodStmt.execute();
                for (AnalysisItem analysisItem : configuredMethod.getKeys()){
                    long methodID = Database.instance().getAutoGenKey(insertMethodStmt);
                    insertItemStmt.setLong(1, methodID);
                    insertItemStmt.setLong(2, analysisItem.getAnalysisItemID());
                    insertItemStmt.execute();
                }
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void deployService(long feedID) {
        Connection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
            FeedStorage feedStorage = new FeedStorage();
            FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(feedID, conn);
            DynamicServiceDefinition definition = getDynamicServiceDefinition(feedID, conn, session);
            if (definition == null) {
                definition = new DynamicServiceDefinition();
                definition.setFeedID(feedID);
                definition.setConfiguredMethods(new ArrayList<ConfiguredMethod>());
                addDynamicServiceDefinition(definition, conn);
                feedDefinition.setDynamicServiceDefinitionID(definition.getServiceID());
            } else {
                APIManager.instance().undeploy(feedID);
            }
            definition.generateCode(feedDefinition, conn);
            definition.deploy(conn);
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LogClass.error(e1);
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LogClass.error(e);
            }
            Database.instance().closeConnection(conn);
        }
    }

    public void deployServiceDefinition(DynamicServiceDefinition dynamicServiceDefinition) {
        try {
            Connection conn = Database.instance().getConnection();
            Session session = Database.instance().createSession(conn);
            try {
                conn.setAutoCommit(false);
                DynamicServiceDefinition existingDefinition = getDynamicServiceDefinition(dynamicServiceDefinition.getFeedID(), conn, session);
                if (existingDefinition != null) {
                    undeployService(dynamicServiceDefinition.getFeedID(), conn);
                }
                addDynamicServiceDefinition(dynamicServiceDefinition, conn);
                conn.commit();
            } catch (SQLException e) {
                LogClass.error(e);
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    LogClass.error(e1);
                }
                throw new RuntimeException(e);
            } finally {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    LogClass.error(e);
                }
                session.close();
                Database.instance().closeConnection(conn);
            }
            deployService(dynamicServiceDefinition.getFeedID());
        } catch (Exception e) {
            LogClass.error(e);
        }
    }

    public void undeployService(long feedID, Connection conn) {
        FeedStorage feedStorage = new FeedStorage();
        FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(feedID, conn);
        try {
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM DYNAMIC_SERVICE_DESCRIPTOR WHERE FEED_ID = ?");
            deleteStmt.setLong(1, feedID);
            deleteStmt.executeUpdate();
            PreparedStatement deleteCodeStmt = conn.prepareStatement("DELETE FROM DYNAMIC_SERVICE_CODE WHERE FEED_ID = ?");
            deleteCodeStmt.setLong(1, feedID);
            deleteCodeStmt.executeUpdate();
            feedDefinition.setDynamicServiceDefinitionID(0);
            feedStorage.updateDataFeedConfiguration(feedDefinition, conn);
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void undeployService(long feedID) {

        Connection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            undeployService(feedID, conn);
            conn.commit();
        } catch (SQLException e) {
            LogClass.error(e);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LogClass.error(e1);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LogClass.error(e);
            }
            Database.instance().closeConnection(conn);
        }
    }
}
