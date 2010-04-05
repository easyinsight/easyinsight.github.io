package com.easyinsight.etl;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.StringValue;
import com.easyinsight.core.Value;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.Roles;
import com.easyinsight.security.SecurityUtil;
import org.hibernate.Session;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: Apr 4, 2010
 * Time: 5:47:30 PM
 */
public class ETLService {

    public void deleteLookupTable(long lookupTableID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_SOURCE_ID FROM LOOKUP_TABLE WHERE LOOKUP_TABLE_ID = ?");
            queryStmt.setLong(1, lookupTableID);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                long dataSourceID = rs.getLong(1);
                SecurityUtil.authorizeFeed(dataSourceID, Roles.OWNER);
                PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM LOOKUP_TABLE WHERE LOOKUP_TABLE_ID = ?");
                deleteStmt.setLong(1, lookupTableID);
                deleteStmt.executeUpdate();
            }
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public List<LookupTableDescriptor> getLookupTableDescriptors() {
        List<LookupTableDescriptor> descriptors = new ArrayList<LookupTableDescriptor>();
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT LOOKUP_TABLE_ID, LOOKUP_TABLE_NAME, DATA_SOURCE_ID FROM LOOKUP_TABLE, UPLOAD_POLICY_USERS " +
                    "WHERE LOOKUP_TABLE.data_source_id = UPLOAD_POLICY_USERS.feed_id  AND UPLOAD_POLICY_USERS.user_id = ?");
            queryStmt.setLong(1, SecurityUtil.getUserID());
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                LookupTableDescriptor lookupTableDescriptor = new LookupTableDescriptor();
                lookupTableDescriptor.setId(rs.getLong(1));
                lookupTableDescriptor.setName(rs.getString(2));
                lookupTableDescriptor.setDataSourceID(rs.getLong(3));
                descriptors.add(lookupTableDescriptor);
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return descriptors;
    }

    public LookupTable getLookupTable(long lookupTableID) {
        LookupTable lookupTable;
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_SOURCE_ID, LOOKUP_TABLE_NAME, SOURCE_ITEM_ID," +
                    "TARGET_ITEM_ID FROM LOOKUP_TABLE WHERE LOOKUP_TABLE_ID = ?");
            queryStmt.setLong(1, lookupTableID);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                long dataSourceID = rs.getLong(1);
                String name = rs.getString(2);
                long sourceItemID = rs.getLong(3);
                long targetItemID = rs.getLong(4);
                AnalysisItem sourceItem = (AnalysisItem) session.createQuery("from AnalysisItem where analysisItemID = ?").setLong(0, sourceItemID).list().get(0);
                sourceItem.afterLoad();
                AnalysisItem targetItem = (AnalysisItem) session.createQuery("from AnalysisItem where analysisItemID = ?").setLong(0, targetItemID).list().get(0);
                targetItem.afterLoad();
                lookupTable = new LookupTable();
                lookupTable.setName(name);
                lookupTable.setDataSourceID(dataSourceID);
                lookupTable.setSourceField(sourceItem);
                lookupTable.setTargetField(targetItem);
                lookupTable.setLookupTableID(lookupTableID);
                PreparedStatement getPairsStmt = conn.prepareStatement("SELECT SOURCE_VALUE, TARGET_VALUE, target_date_value FROM " +
                        "LOOKUP_PAIR WHERE LOOKUP_TABLE_ID = ?");
                getPairsStmt.setLong(1, lookupTableID);
                ResultSet pairsRS = getPairsStmt.executeQuery();
                List<LookupPair> pairs = new ArrayList<LookupPair>();
                while (pairsRS.next()) {
                    String sourceValue = pairsRS.getString(1);
                    Value value;
                    if (targetItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                        Timestamp targetDate = pairsRS.getTimestamp(3);
                        if (pairsRS.wasNull()) {
                            value = new EmptyValue();
                        } else {
                            value = new DateValue(new Date(targetDate.getTime()));
                        }
                    } else {
                        String targetValue = pairsRS.getString(2);
                        value = new StringValue(targetValue);
                    }
                    LookupPair lookupPair = new LookupPair();
                    lookupPair.setSourceValue(new StringValue(sourceValue));
                    lookupPair.setTargetValue(value);
                    pairs.add(lookupPair);
                }
                lookupTable.setLookupPairs(pairs);
            } else {
                throw new RuntimeException();
            }
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
        return lookupTable;
    }

    public long saveNewLookupTable(LookupTable lookupTable) {
        long id;
        SecurityUtil.authorizeFeed(lookupTable.getDataSourceID(), Roles.OWNER);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(lookupTable.getDataSourceID(), conn);
            dataSource.getFields().add(lookupTable.getTargetField());
            new FeedStorage().updateDataFeedConfiguration(dataSource, conn);
            PreparedStatement insertTableStmt = conn.prepareStatement("INSERT INTO LOOKUP_TABLE (DATA_SOURCE_ID," +
                    "LOOKUP_TABLE_NAME, SOURCE_ITEM_ID, TARGET_ITEM_ID) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertTableStmt.setLong(1, lookupTable.getDataSourceID());
            insertTableStmt.setString(2, lookupTable.getName());
            insertTableStmt.setLong(3, lookupTable.getSourceField().getAnalysisItemID());
            insertTableStmt.setLong(4, lookupTable.getTargetField().getAnalysisItemID());
            insertTableStmt.execute();
            id = Database.instance().getAutoGenKey(insertTableStmt);
            lookupTable.getTargetField().setLookupTableID(id);
            new FeedStorage().updateDataFeedConfiguration(dataSource, conn);
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
        return id;
    }

    private void savePairs(long id, AnalysisItem analysisItem, List<LookupPair> pairs, EIConnection conn) throws SQLException {
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM LOOKUP_PAIR WHERE LOOKUP_TABLE_ID = ?");
        clearStmt.setLong(1, id);
        clearStmt.executeUpdate();
        if (analysisItem.getType() == AnalysisItemTypes.DIMENSION) {
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO LOOKUP_PAIR (LOOKUP_TABLE_ID, " +
                    "SOURCE_VALUE, TARGET_VALUE) VALUES (?, ?, ?)");
            for (LookupPair lookupPair : pairs) {
                insertStmt.setLong(1, id);
                insertStmt.setString(2, lookupPair.getSourceValue().toString());
                insertStmt.setString(3, lookupPair.getTargetValue().toString());
                insertStmt.execute();
            }
        } else if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO LOOKUP_PAIR (LOOKUP_TABLE_ID, " +
                    "SOURCE_VALUE, target_date_value) VALUES (?, ?, ?)");
            for (LookupPair lookupPair : pairs) {
                insertStmt.setLong(1, id);
                insertStmt.setString(2, lookupPair.getSourceValue().toString());
                DateValue dateValue = (DateValue) lookupPair.getTargetValue();
                if (dateValue.getDate() == null) {
                    insertStmt.setNull(3, Types.TIMESTAMP);
                } else {
                    insertStmt.setTimestamp(3, new java.sql.Timestamp(dateValue.getDate().getTime()));
                }
                insertStmt.execute();
            }
        }
    }

    public void updateLookupTable(LookupTable lookupTable) {
        SecurityUtil.authorizeFeed(lookupTable.getDataSourceID(), Roles.OWNER);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE LOOKUP_TABLE SET LOOKUP_TABLE_NAME = ? WHERE " +
                    "LOOKUP_TABLE_ID = ?");
            updateStmt.setString(1, lookupTable.getName());
            updateStmt.setLong(2, lookupTable.getLookupTableID());
            updateStmt.executeUpdate();
            savePairs(lookupTable.getLookupTableID(), lookupTable.getTargetField(), lookupTable.getLookupPairs(), conn);
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }
}
