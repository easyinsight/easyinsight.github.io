package com.easyinsight.pipeline;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemHandle;
import com.easyinsight.analysis.DataResults;
import com.easyinsight.analysis.IRow;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

import java.util.*;

/**
 * User: jamesboe
 * Date: 12/17/13
 * Time: 11:41 AM
 */
public class DrillthroughComponent implements IComponent {

    private AnalysisItemHandle passThroughField;

    public DrillthroughComponent(AnalysisItemHandle passThroughField) {
        this.passThroughField = passThroughField;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        AnalysisItem analysisItem1 = null;
        List<AnalysisItem> allItems = pipelineData.getAllItems();

        for (AnalysisItem field : allItems) {
            if ((passThroughField.getAnalysisItemID() != null && passThroughField.getAnalysisItemID() > 0 && field.getAnalysisItemID() == passThroughField.getAnalysisItemID()) ||
                    passThroughField.getName().equals(field.toDisplay())) {
                analysisItem1 = field;
                break;
            }
        }

        if (analysisItem1 == null) {
            throw new RuntimeException();
        }

        for (IRow row : dataSet.getRows()) {
            Value value = row.getValue(analysisItem1);
            Map<String, Set<Value>> map = row.getPassthroughRow();
            if (map == null) {
                map = new HashMap<String, Set<Value>>();
                row.setPassthroughRow(map);
            }
            Set<Value> values = map.get(analysisItem1.qualifiedName());
            if (values == null) {
                values = new HashSet<Value>();
                map.put(analysisItem1.qualifiedName(), values);
            }
            values.add(value);
            for (AnalysisItem measure : pipelineData.getReportItems()) {
                Value measureValue = row.getValue(measure);
                Map<String, List<Value>> dtValues = measureValue.getDrillThroughs();
                if (dtValues == null) {
                    dtValues = new HashMap<String, List<Value>>();
                    measureValue.setDrillThroughs(dtValues);
                }
                List<Value> vals = dtValues.get(analysisItem1.qualifiedName());
                if (vals == null) {
                    vals = new ArrayList<Value>();
                    dtValues.put(analysisItem1.qualifiedName(), vals);
                }
                vals.add(value);
            }
        }
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {

    }
}
