package com.easyinsight.dataset;

import com.easyinsight.stream.google.IDataTypeGuesser;
import com.easyinsight.stream.google.DataTypeGuesser;
import com.easyinsight.IRow;
import com.easyinsight.AnalysisItem;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.core.EmptyValue;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: May 31, 2008
 * Time: 1:00:29 PM
 */
public class ColumnSegmentFactory {

    private List<AnalysisItem> fields = new ArrayList<AnalysisItem>();

    public PersistableDataSetForm createPersistableForm(DataSet dataSet) {
        populateFields(dataSet);
        return createPersistableForm(dataSet, fields);
    }

    public PersistableDataSetForm createPersistableForm(DataSet dataSet, List<AnalysisItem> fields) {

        Map<Key, Value[]> valuesMap = new HashMap<Key, Value[]>();
        for (AnalysisItem feedItem : fields) {
            Value[] values = new Value[dataSet.getRows().size()];
            valuesMap.put(feedItem.getKey(), values);
        }
        for (int i = 0; i < dataSet.getRows().size(); i++) {
            IRow row = dataSet.getRow(i);
            for (AnalysisItem feedItem : fields) {
                Value value = row.getValue(feedItem.getKey());
                Value[] values = valuesMap.get(feedItem.getKey());
                if (value == null) {
                    value = new EmptyValue();
                }
                values[i] = value;
            }
        }
        Map<Key, ColumnSegment> segmentMap = new HashMap<Key, ColumnSegment>();
        for (Map.Entry<Key, Value[]> entry : valuesMap.entrySet()) {
            segmentMap.put(entry.getKey(), new ColumnSegment(entry.getValue()));
        }
        return new PersistableDataSetForm(segmentMap);
    }

    public List<AnalysisItem> getFields() {
        return fields;
    }

    private void populateFields(DataSet dataSet) {
        IDataTypeGuesser guesser = new DataTypeGuesser();
        for (IRow row : dataSet.getRows()) {
            for (Key key : row.getKeys()) {
                Value value = row.getValue(key);
                if (value == null) {
                    value = new EmptyValue();
                }
                guesser.addValue(key, value);
            }
        }
        fields = guesser.createFeedItems();
    }
}
