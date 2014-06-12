package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSLineChartDefinition;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

import java.util.*;

/**
 * User: jamesboe
 * Date: 2/15/13
 * Time: 9:39 AM
 */
public class LineChartComponent implements IComponent {

    private Date max;
    private List<Map<String, Value>> trendLineValues;

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        if (pipelineData.getReport() != null) {
            Calendar calendar = Calendar.getInstance();
            WSLineChartDefinition lineChartDefinition = (WSLineChartDefinition) pipelineData.getReport();
            if (lineChartDefinition.isFillInZero()) {
                lineChartDefinition.fillInZeroes(dataSet, pipelineData.getInsightRequestMetadata());
            }
            max = lineChartDefinition.getXAxisMaximum();
            int interval;
            if ("Year".equals(lineChartDefinition.getTrendLineTimeInterval())) {
                interval = AnalysisDateDimension.YEAR_LEVEL;
            } else if ("Month".equals(lineChartDefinition.getTrendLineTimeInterval())) {
                interval = AnalysisDateDimension.MONTH_LEVEL;
            } else if ("Week".equals(lineChartDefinition.getTrendLineTimeInterval())) {
                interval = AnalysisDateDimension.WEEK_LEVEL;
            } else if ("Quarter".equals(lineChartDefinition.getTrendLineTimeInterval())) {
                interval = AnalysisDateDimension.QUARTER_OF_YEAR_LEVEL;
            } else {
                interval = 0;
            }
            if (interval != 0) {
                AnalysisMeasure measure;
                if (!lineChartDefinition.isMultiMeasure()) {
                    measure = (AnalysisMeasure) lineChartDefinition.getMeasure();
                } else {
                    measure = (AnalysisMeasure) lineChartDefinition.getMeasures().get(0);
                }
                AnalysisDateDimension trend = new AnalysisDateDimension(lineChartDefinition.getXaxis().getKey(), true, interval);
                AnalysisDateDimension date = (AnalysisDateDimension) lineChartDefinition.getXaxis();
                Map<Value, Aggregation> map = new LinkedHashMap<Value, Aggregation>();
                for (IRow row : dataSet.getRows()) {
                    Value xAxisValue = row.getValue(lineChartDefinition.getXaxis());
                    Value measureValue = row.getValue(measure);
                    Value aggregated = trend.transformValue(xAxisValue, new InsightRequestMetadata(), false, calendar);
                    Aggregation aggregation = map.get(aggregated);
                    if (aggregation == null) {
                        aggregation = new AggregationFactory(measure, false).getAggregation(AggregationTypes.AVERAGE);
                        map.put(aggregated, aggregation);
                    }
                    aggregation.addValue(measureValue);
                }
                List<Map<String, Value>> values = new ArrayList<Map<String, Value>>();
                for (Map.Entry<Value, Aggregation> entry : map.entrySet()) {
                    Map<String, Value> map1 = new HashMap<String, Value>();
                    map1.put("date", entry.getKey());
                    map1.put("trend", entry.getValue().getValue());
                    values.add(map1);
                }
                Calendar cal = Calendar.getInstance();
                Calendar shiftedCal = Calendar.getInstance();
                int time = pipelineData.getInsightRequestMetadata().getUtcOffset() / 60;
                String string;
                if (time > 0) {
                    string = "GMT-"+Math.abs(time);
                } else if (time < 0) {
                    string = "GMT+"+Math.abs(time);
                } else {
                    string = "GMT";
                }
                TimeZone timeZone = TimeZone.getTimeZone(string);
                shiftedCal.setTimeZone(timeZone);
                for (Map<String, Value> map1 : values) {
                    Value value = map1.get("date");
                    if (value.type() == Value.DATE) {
                        DateValue dateValue = (DateValue) value;
                        dateValue.calculate(date.isTimeshift(pipelineData.getInsightRequestMetadata()) ? shiftedCal : cal);
                    }
                }
                trendLineValues = values;
            }
        }

        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
        if (max != null) {
            listDataResults.getAdditionalProperties().put("xAxisMaximum", max);

        }
        if (trendLineValues != null) {
            listDataResults.getAdditionalProperties().put("trendLineData", trendLineValues);
        }
    }
}
