package com.easyinsight.pipeline;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Value;
import com.easyinsight.core.DateValue;
import com.easyinsight.analysis.*;

import java.util.*;

/**
 * User: James Boe
 * Date: May 18, 2009
 * Time: 1:55:39 PM
 */
public class StepCorrelationComponent implements IComponent {

    private AnalysisStep analysisStep;

    public StepCorrelationComponent(AnalysisStep analysisStep) {
        this.analysisStep = analysisStep;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        Map<Value, Correlation> correlationMap = new HashMap<Value, Correlation>();
        for (IRow row : dataSet.getRows()) {
            Value correlationValue = row.getValue(analysisStep.getCorrelationDimension());
            Correlation correlation = correlationMap.get(correlationValue);
            if (correlation == null) {
                correlation = new Correlation(analysisStep.getStartDate(), analysisStep.getEndDate());
                correlationMap.put(correlationValue, correlation);
            }
            correlation.addRow(row);
        }
        List<IRow> rows = new ArrayList<IRow>();
        for (Correlation correlation : correlationMap.values()) {
            rows.addAll(correlation.generateRows());
        }
        return new DataSet(rows);
    }

    public void decorate(DataResults listDataResults) {
    }

    private class Correlation {
        private IRow row;
        private Date startDate;
        private Date endDate;
        private AnalysisDateDimension start;
        private AnalysisDateDimension end;

        private Correlation(AnalysisDateDimension start, AnalysisDateDimension end) {
            this.start = start;
            this.end = end;
        }

        public void addRow(IRow row) {
            this.row = row;
            Value startValue = row.getValue(start);
            if (startValue.type() == Value.DATE) {
                DateValue dateValue = (DateValue) startValue;
                startDate = dateValue.getDate();
            }
            Value endValue = row.getValue(end);
            if (endValue.type() == Value.DATE) {
                DateValue dateValue = (DateValue) endValue;
                endDate = dateValue.getDate();
            }
        }

        public List<IRow> generateRows() {
            // loop from startDate to endDate, create entry for each field
            // create startDate, create endDate, create the interval between
            List<IRow> rows = new ArrayList<IRow>();
            if (startDate != null) {
                long startTime = startDate.getTime();
                long endTime = endDate == null ? System.currentTimeMillis() : endDate.getTime();
                long interval = 1000 * 60 * 60 * 24;
                for (; startTime < endTime; startTime += interval) {
                    IRow row = new Row();
                    row.addValues(this.row.getValues());
                    row.addValue(analysisStep.getKey(), new DateValue(new Date(startTime)));
                    row.removeValue(start.createAggregateKey());
                    row.removeValue(end.createAggregateKey());
                    row.removeValue(analysisStep.getCorrelationDimension().createAggregateKey());
                    rows.add(row);
                }
            }
            return rows;
        }
    }
}
