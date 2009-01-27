package com.easyinsight.api.dynamic;

import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.TableDefinitionMetadata;
import com.easyinsight.storage.IWhere;
import com.easyinsight.storage.StringWhere;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.AnalysisItem;
import com.easyinsight.IRow;
import com.easyinsight.logging.LogClass;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

/**
 * User: James Boe
 * Date: Aug 28, 2008
 * Time: 2:11:17 PM
 */
public class InboundData {

    private static FeedStorage feedStorage = new FeedStorage();

    public static void addInboundData(long feedID, Map<String, Value> data) {
        FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(feedID);
        List<AnalysisItem> fields = feedDefinition.getFields();
        Map<String, Key> keyMap = new HashMap<String, Key>();
        for (AnalysisItem field : fields) {
            keyMap.put(field.getKey().toKeyString(), field.getKey());
        }
        DataSet dataSet = new DataSet();
        IRow row = dataSet.createRow();
        for (Map.Entry<String, Value> entry : data.entrySet()) {
            Key key = keyMap.get(entry.getKey());
            Value value = entry.getValue();
            if (value == null) {
                value = new EmptyValue();
            }
            row.addValue(key, value);
        }
        TableDefinitionMetadata metadata = TableDefinitionMetadata.writeConnection(fields, feedDefinition.getDataFeedID());
        try {
            metadata.insertData(dataSet);
            metadata.commit();
        } catch (SQLException e) {
            LogClass.error(e);
            metadata.rollback();
            throw new RuntimeException(e);
        } finally {
            metadata.closeConnection();
        }
        //feedDefinition.setSize(size);
        //new FeedStorage().updateDataFeedConfiguration(feedDefinition);
    }

    public static void addInboundData(long feedID, List<Map<String, Value>> dataList) {
        FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(feedID);
        List<AnalysisItem> fields = feedDefinition.getFields();
        Map<String, Key> keyMap = new HashMap<String, Key>();
        for (AnalysisItem field : fields) {
            keyMap.put(field.getKey().toKeyString(), field.getKey());
        }
        DataSet dataSet = new DataSet();
        for (Map<String, Value> data : dataList) {
            IRow row = dataSet.createRow();
            for (Map.Entry<String, Value> entry : data.entrySet()) {
                Key key = keyMap.get(entry.getKey());
                Value value = entry.getValue();
                if (value == null) {
                    value = new EmptyValue();
                }
                row.addValue(key, value);
            }
        }
        TableDefinitionMetadata metadata = TableDefinitionMetadata.writeConnection(fields, feedDefinition.getDataFeedID());
        try {
            metadata.insertData(dataSet);
            metadata.commit();
        } catch (SQLException e) {
            LogClass.error(e);
            metadata.rollback();
            throw new RuntimeException(e);
        } finally {
            metadata.closeConnection();
        }
    }

    public static void replaceInboundData(long feedID, List<Map<String, Value>> dataList) {
        FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(feedID);
        List<AnalysisItem> fields = feedDefinition.getFields();
        Map<String, Key> keyMap = new HashMap<String, Key>();
        for (AnalysisItem field : fields) {
            keyMap.put(field.getKey().toKeyString(), field.getKey());
        }
        
        DataSet dataSet = new DataSet();
        for (Map<String, Value> data : dataList) {
            IRow row = dataSet.createRow();
            for (Map.Entry<String, Value> entry : data.entrySet()) {
                Key key = keyMap.get(entry.getKey());
                Value value = entry.getValue();
                if (value == null) {
                    value = new EmptyValue();
                }
                row.addValue(key, value);
            }
        }
        TableDefinitionMetadata metadata = TableDefinitionMetadata.writeConnection(fields, feedDefinition.getDataFeedID());
        try {
            metadata.truncate();
            metadata.insertData(dataSet);
            metadata.commit();
        } catch (SQLException e) {
            LogClass.error(e);
            metadata.rollback();
            throw new RuntimeException(e);
        } finally {
            metadata.closeConnection();
        }
    }

    public static void updateInboundData(long feedID, List<Map<String, Value>> dataList, Map<String, Value> updates) {
        FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(feedID);
        List<AnalysisItem> fields = feedDefinition.getFields();
        Map<String, Key> keyMap = new HashMap<String, Key>();
        for (AnalysisItem field : fields) {
            keyMap.put(field.getKey().toKeyString(), field.getKey());
        }
        DataSet dataSet = new DataSet();
        for (Map<String, Value> data : dataList) {
            IRow row = dataSet.createRow();
            for (Map.Entry<String, Value> entry : data.entrySet()) {
                Key key = keyMap.get(entry.getKey());
                Value value = entry.getValue();
                if (value == null) {
                    value = new EmptyValue();
                }
                row.addValue(key, value);
            }
        }
        List<IWhere> wheres = new ArrayList<IWhere>();
        for (Map.Entry<String, Value> updateEntry : updates.entrySet()) {
            Key key = keyMap.get(updateEntry.getKey());
            wheres.add(new StringWhere(key, updateEntry.getValue().toString()));
        }
        TableDefinitionMetadata metadata = TableDefinitionMetadata.writeConnection(fields, feedDefinition.getDataFeedID());
        try {
            metadata.updateData(dataSet, wheres);
            metadata.commit();
        } catch (SQLException e) {
            LogClass.error(e);
            metadata.rollback();
            throw new RuntimeException(e);
        } finally {
            metadata.closeConnection();
        }
    }
}
