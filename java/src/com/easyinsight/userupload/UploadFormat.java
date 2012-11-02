package com.easyinsight.userupload;

import com.easyinsight.dataset.PersistableDataSetForm;
import com.easyinsight.dataset.ColumnSegment;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.core.Value;
import com.easyinsight.core.EmptyValue;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.sql.Connection;
import java.sql.SQLException;
import java.io.Serializable;

/**
 * User: James Boe
 * Date: Apr 28, 2008
 * Time: 6:45:32 PM
 */
public abstract class UploadFormat implements Serializable {

    public static final int VERTICAL_HEADERS = 1;
    public static final int HORIZONTAL_HEADERS = 2;

    public abstract boolean test(byte[] data);

    public UserUploadAnalysis analyze(byte[] data) {
        DataTypeGuesser dataTypeGuesser = createDataTypeGuesser(data);
        UserUploadAnalysis userUploadAnalysis = new UserUploadAnalysis(dataTypeGuesser.createFeedItems());
        userUploadAnalysis.setSampleMap(dataTypeGuesser.getGuessesMap());
        userUploadAnalysis.setSize(data.length);
        return userUploadAnalysis;
    }

    public PersistableDataSetForm createDataSet(byte[] data, List<AnalysisItem> fields, IUploadFormatMapper formatMapper) {
        return createInternalDataSet(data, null, fields, formatMapper);
    }

    private DataTypeGuesser createDataTypeGuesser(byte[] data) {
        DataTypeGuesser dataTypeGuesser = new DataTypeGuesser();
        createInternalDataSet(data, dataTypeGuesser, null, new DefaultFormatMapper(null));
        return dataTypeGuesser;
    }

    private Map<String, Key> createKeyMap(List<AnalysisItem> analysisItems) {
        Map<String, Key> keyMap = null;
        if (analysisItems != null) {
            keyMap = new HashMap<String, Key>();
            for (AnalysisItem analysisItem : analysisItems) {
                keyMap.put(((NamedKey) analysisItem.getKey()).getName(), analysisItem.getKey());
            }
        }
        return keyMap;
    }

    private PersistableDataSetForm createInternalDataSet(byte[] data, IDataTypeGuesser dataTypeGuesser, List<AnalysisItem> analysisItems, IUploadFormatMapper formatMapper) {
        Map<String, Key> keyMap = createKeyMap(analysisItems);
        Map<String, AnalysisItem> analysisItemMap = null;
        if (analysisItems != null) {
            analysisItemMap = new HashMap<String, AnalysisItem>();
            for (AnalysisItem analysisItem : analysisItems) {
                analysisItemMap.put(analysisItem.getKey().toDisplayName(), analysisItem);
            }
        }
        // special case = key by column
        // or, we always partition by column...

        try {

            GridData gridData = createGridData(data, dataTypeGuesser, keyMap, analysisItemMap);

            Map<Key, ColumnSegment> columnSegmentMap = new LinkedHashMap<Key, ColumnSegment>();
            for (int j = 0; j < gridData.headerColumns.length; j++) {
                Value[] verticalGridSlice = new Value[gridData.rowCount];
                for (int i = 0; i < gridData.rowCount; i++) {
                    Value value;
                    if (j >= gridData.grid[i].length) {
                        value = new EmptyValue();
                    } else {
                        value = gridData.grid[i][j];
                    }
                    verticalGridSlice[i] = value;
                }
                Key key = formatMapper.reconcileKey(createName(gridData.headerColumns[j], j));

                if (key != null) {
                    columnSegmentMap.put(key, new ColumnSegment(verticalGridSlice));
                }
            }

            return new PersistableDataSetForm(columnSegmentMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String createName(String key, int index) {
        if (key == null || "".equals(key.trim())) {
            return String.valueOf("Column " + index);
        }
        if (key.length() > 50) {
            key = key.substring(0, 50);
        }
        return key.trim();
    }

    protected abstract GridData createGridData(byte[] data, IDataTypeGuesser dataTypeGuesser, Map<String, Key> keyMap, Map<String, AnalysisItem> analysisItems);

    public abstract void persist(Connection conn, long feedID) throws SQLException;
}
