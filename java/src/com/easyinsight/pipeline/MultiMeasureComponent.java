package com.easyinsight.pipeline;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.DataResults;
import com.easyinsight.analysis.IRow;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

import java.util.List;

/**
 * User: jamesboe
 * Date: 2/27/13
 * Time: 8:44 AM
 */
public class MultiMeasureComponent implements IComponent {

    private List<AnalysisItem> measures;

    private AnalysisItem groupingTarget;
    private AnalysisItem measureTarget;

    private String name;

    public MultiMeasureComponent(String name, List<AnalysisItem> measures, AnalysisItem groupingTarget, AnalysisItem measureTarget) {
        this.name = name;
        this.measures = measures;
        this.groupingTarget = groupingTarget;
        this.measureTarget = measureTarget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MultiMeasureComponent that = (MultiMeasureComponent) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        try {
            DataSet targetSet = new DataSet();
            for (IRow row : dataSet.getRows()) {
                for (AnalysisItem measure : measures) {
                    IRow targetRow = row.clone();
                    Value measureValue = row.getValue(measure);
                    row.removeValue(measure.createAggregateKey());
                    targetRow.addValue(measureTarget.createAggregateKey(), measureValue);
                    targetRow.addValue(groupingTarget.createAggregateKey(), measure.toDisplay());
                    targetSet.addRow(targetRow);
                }
            }
            return targetSet;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public void decorate(DataResults listDataResults) {
    }
}
