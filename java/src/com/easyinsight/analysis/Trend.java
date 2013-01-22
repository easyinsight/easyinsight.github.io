package com.easyinsight.analysis;

import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.kpi.KPI;
import com.easyinsight.kpi.KPIOutcome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 9/27/11
 * Time: 3:57 PM
 */
public class Trend {

    public List<TrendOutcome> calculateTrends(List<? extends AnalysisItem> measures, List<AnalysisItem> groupings, DataSet nowSet, DataSet pastSet) {
        List<TrendOutcome> trendOutcomes = new ArrayList<TrendOutcome>();
        for (AnalysisItem analysisItem : measures) {
            AnalysisMeasure analysisMeasure = (AnalysisMeasure) analysisItem;
            //TrendOutcome trendOutcome = new TrendOutcome();

            Map<Map<String, Value>, TrendOutcome> outcomeMap = new HashMap<Map<String, Value>, TrendOutcome>();
            if (nowSet.getRows().size() > 0) {
                for (IRow row : nowSet.getRows()) {
                    Map<String, Value> map = new HashMap<String, Value>();
                    if (groupings != null) {
                        for (AnalysisItem grouping : groupings) {
                            map.put(grouping.qualifiedName(), row.getValue(grouping));
                        }
                    }
                    TrendOutcome trendOutcome = new TrendOutcome();
                    trendOutcome.setNow(row.getValue(analysisMeasure.createAggregateKey()));
                    if (analysisItem.getReportFieldExtension() != null && analysisItem.getReportFieldExtension() instanceof TrendReportFieldExtension &&
                            ((TrendReportFieldExtension) analysisItem.getReportFieldExtension()).getTrendComparisonField() != null) {
                        Value previousValue = row.getValue(((TrendReportFieldExtension) analysisItem.getReportFieldExtension()).getTrendComparisonField());
                        trendOutcome.setHistorical(previousValue);
                    }
                    outcomeMap.put(map, trendOutcome);
                }

                //newValue = trendOutcome.getNow().toDouble();
            }
            boolean needPastSet = false;
            if (analysisItem.getReportFieldExtension() != null && analysisItem.getReportFieldExtension() instanceof TrendReportFieldExtension &&
                    ((TrendReportFieldExtension) analysisItem.getReportFieldExtension()).getTrendComparisonField() != null) {
                needPastSet = true;
            }
            if (!needPastSet && pastSet.getRows().size() > 0) {
                for (IRow row : pastSet.getRows()) {
                    Map<String, Value> map = new HashMap<String, Value>();
                    if (groupings != null) {
                        for (AnalysisItem grouping : groupings) {
                            map.put(grouping.qualifiedName(), row.getValue(grouping));
                        }
                    }
                    TrendOutcome trendOutcome = outcomeMap.get(map);
                    if (trendOutcome == null) {
                        trendOutcome = new TrendOutcome();
                        outcomeMap.put(map, trendOutcome);
                    }
                    trendOutcome.setHistorical(row.getValue(analysisMeasure.createAggregateKey()));
                    outcomeMap.put(map, trendOutcome);
                }

                /*trendOutcome.setHistorical(pastSet.getRow(0).getValue(analysisMeasure.createAggregateKey()));
                oldValue = trendOutcome.getHistorical().toDouble();*/
            }
            for (Map.Entry<Map<String, Value>, TrendOutcome> entry : outcomeMap.entrySet()) {
                TrendOutcome trendOutcome = entry.getValue();
                if (trendOutcome.getNow() == null) {
                    trendOutcome.setNow(new EmptyValue());
                }
                if (trendOutcome.getHistorical() == null) {
                    trendOutcome.setHistorical(new EmptyValue());
                }

                double newValue = trendOutcome.getNow().toDouble();
                double oldValue = trendOutcome.getHistorical().toDouble();
                if (newValue != 0 && oldValue != 0) {
                    double delta = newValue - oldValue;
                    int direction;
                    if (delta > 0) {
                        direction = KPIOutcome.UP_DIRECTION;
                    } else if (delta < 0) {
                        direction = KPIOutcome.DOWN_DIRECTION;
                    } else {
                        direction = KPIOutcome.NO_DIRECTION;
                    }
                    int outcome = populateOutcome(analysisMeasure.isHighIsGood() ? KPI.GOOD : KPI.BAD, delta, newValue, 0);
                    trendOutcome.setDirection(direction);
                    trendOutcome.setOutcome(outcome);
                }

                trendOutcome.setDimensions(entry.getKey());
                trendOutcome.setMeasure(analysisMeasure);
                trendOutcomes.add(trendOutcome);
            }


        }
        return trendOutcomes;
    }

    private int populateOutcome(int highIsGood, double delta, double endValue, double tolerance) {
        int outcome;
        double thresholdValue = Math.abs(endValue * (tolerance / 100));
        if (highIsGood == KPI.GOOD) {
            if (Math.abs(delta) <= thresholdValue) {
                outcome = KPIOutcome.NEUTRAL;
            } else if (delta > 0) {
                outcome = KPIOutcome.POSITIVE;
            } else {
                outcome = KPIOutcome.NEGATIVE;
            }
        } else if (highIsGood == KPI.BAD) {
            if (Math.abs(delta) <= thresholdValue) {
                outcome = KPIOutcome.NEUTRAL;
            } else if (delta < 0) {
                outcome = KPIOutcome.POSITIVE;
            } else {
                outcome = KPIOutcome.NEGATIVE;
            }
        } else {
            outcome = KPIOutcome.NEUTRAL;
        }
        return outcome;
    }
}
