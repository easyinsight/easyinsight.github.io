package com.easyinsight.analysis;

import com.easyinsight.analysis.definitions.WSCompareYearsDefinition;
import com.easyinsight.analysis.definitions.WSYTDDefinition;
import com.easyinsight.calculations.CalcGraph;
import com.easyinsight.core.*;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.pipeline.CalculationComponent;
import com.easyinsight.pipeline.IComponent;
import com.easyinsight.pipeline.PipelineData;

import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: 12/3/11
 * Time: 5:25 PM
 */
public class YTDUtil {

    public static YearStuff getYearStuff(WSCompareYearsDefinition yearsDefinition, DataSet nowSet, PipelineData pipelineData, Set<AnalysisItem> reportItems) {
        AnalysisItem timeDimension = yearsDefinition.getTimeDimension();
        Collection<AnalysisMeasure> measures = new ArrayList<AnalysisMeasure>();
        Collection<AnalysisMeasure> realMeasures = new ArrayList<AnalysisMeasure>();
        for (AnalysisItem analysisItem : pipelineData.getAllRequestedItems()) {
            if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                measures.add((AnalysisMeasure) analysisItem);
            }
        }
        for (AnalysisItem analysisItem : yearsDefinition.getMeasures()) {
            realMeasures.add((AnalysisMeasure) analysisItem);
        }
        Map<Integer, Map<AnalysisMeasure, Aggregation>> map = new HashMap<Integer, Map<AnalysisMeasure, Aggregation>>();
        Set<Integer> years = new HashSet<Integer>();
        Calendar cal = Calendar.getInstance();
        for (IRow row : nowSet.getRows()) {
            Value year = row.getValue(timeDimension);
            if (year.type() == Value.DATE) {
                DateValue dateValue = (DateValue) year;
                cal.setTime(dateValue.getDate());
                int yearVal = cal.get(Calendar.YEAR);
                if (yearVal < 2008) {
                    continue;
                }
                years.add(yearVal);

                Map<AnalysisMeasure, Aggregation> yearMap = map.get(yearVal);
                if (yearMap == null) {
                    yearMap = new HashMap<AnalysisMeasure, Aggregation>();
                    map.put(yearVal, yearMap);
                    for (AnalysisMeasure analysisMeasure : measures) {
                        Aggregation aggregation = new AggregationFactory(analysisMeasure, false).getAggregation();
                        yearMap.put(analysisMeasure, aggregation);
                    }
                }
                for (AnalysisMeasure analysisMeasure : measures) {
                    Aggregation aggregation = yearMap.get(analysisMeasure);
                    aggregation.addValue(row.getValue(analysisMeasure));
                }
            }
        }
        List<IComponent> components = new CalcGraph().doFunGraphStuff(reportItems, pipelineData.getAllItems(), reportItems, true, new AnalysisItemRetrievalStructure());
        components.addAll(new CalcGraph().doFunGraphStuff(reportItems, pipelineData.getAllItems(), reportItems, false, new AnalysisItemRetrievalStructure()));
        Iterator<IComponent> iter = components.iterator();
        while (iter.hasNext()) {
            IComponent component = iter.next();
            if (!(component instanceof CalculationComponent)) {
                iter.remove();
                continue;
            }
            CalculationComponent calculationComponent = (CalculationComponent) component;
            if (calculationComponent.getAnalysisCalculation().getAggregation() != AggregationTypes.AVERAGE) {
                iter.remove();
            }
        }
        DataSet tempSet = new DataSet();
        IRow tempRow = tempSet.createRow();
        for (Map<AnalysisMeasure, Aggregation> aggMap : map.values()) {
            for (Map.Entry<AnalysisMeasure, Aggregation> entry : aggMap.entrySet()) {
                tempRow.addValue(entry.getKey().createAggregateKey(), entry.getValue().getValue());
            }
            for (IComponent component : components) {
                component.apply(tempSet, pipelineData);
            }
            IRow tempRow1 = tempSet.getRow(0);
            for (AnalysisMeasure measure : realMeasures) {
                if (measure.hasType(AnalysisItemTypes.CALCULATION) && measure.getAggregation() == AggregationTypes.AVERAGE) {
                    Aggregation aggregation = new AggregationFactory(measure, false).getAggregation();
                    aggregation.addValue(tempRow1.getValue(measure));
                    aggMap.put(measure, aggregation);
                }
            }
        }
        List<Integer> sortedYears = new ArrayList<Integer>(years);
        Collections.sort(sortedYears);
        List<CompareYearsRow> rows = new ArrayList<CompareYearsRow>();
        Set<PercentChangeItem> percentChangeItems = new HashSet<PercentChangeItem>();
        for (AnalysisMeasure analysisMeasure : realMeasures) {
            CompareYearsRow compareYearsRow = new CompareYearsRow();
            compareYearsRow.setMeasure(analysisMeasure);
            for (int i = 0; i < sortedYears.size(); i++) {
                Integer year = sortedYears.get(i);
                Value yearValue = map.get(year).get(analysisMeasure).getValue();
                String formattedYear = String.valueOf(year);
                CompareYearsResult compareYearsResult = new CompareYearsResult();
                compareYearsResult.setHeader(new StringValue(formattedYear));
                compareYearsResult.setValue(yearValue);
                compareYearsResult.setPercentChange(false);
                compareYearsRow.getResults().put(formattedYear, compareYearsResult);
                if (i > 0) {
                    Integer previousYear = sortedYears.get(i - 1);
                    Value previousYearValue = map.get(previousYear).get(analysisMeasure);
                    Value change = new NumericValue((yearValue.toDouble() - previousYearValue.toDouble()) / previousYearValue.toDouble() * 100);
                    CompareYearsResult percentChangeResult = new CompareYearsResult();
                    String formattedChange = String.valueOf(previousYear).substring(2, 4) + "-" + formattedYear.substring(2, 4) + "%";
                    percentChangeItems.add(new PercentChangeItem(formattedChange, Integer.parseInt(formattedYear)));
                    percentChangeResult.setHeader(new StringValue(formattedChange));
                    percentChangeResult.setPercentChange(true);
                    percentChangeResult.setValue(change);
                    compareYearsRow.getResults().put(formattedChange, percentChangeResult);
                }
            }
            rows.add(compareYearsRow);
        }
        List<PercentChangeItem> sortablePercentChangeItemList = new ArrayList<PercentChangeItem>(percentChangeItems);
        Collections.sort(sortablePercentChangeItemList);
        List<String> headers = new ArrayList<String>();
        for (Integer yearVal : sortedYears) {
            String formattedYear = String.valueOf(yearVal);
            headers.add(formattedYear);
        }
        if (headers.size() > 2) {
            headers.add(headers.size() - 1, sortablePercentChangeItemList.get(1).label);
        }
        if (headers.size() > 1) {
            headers.add(sortablePercentChangeItemList.get(0).label);
        }

        return new YearStuff(headers, rows);
    }

    private static class PercentChangeItem implements Comparable<PercentChangeItem>{
        private String label;
        private Integer tailYear;

        private PercentChangeItem(String label, int tailYear) {
            this.label = label;
            this.tailYear = tailYear;
        }

        public int compareTo(PercentChangeItem percentChangeItem) {
            return percentChangeItem.tailYear.compareTo(tailYear);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PercentChangeItem that = (PercentChangeItem) o;

            if (label != null ? !label.equals(that.label) : that.label != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return label != null ? label.hashCode() : 0;
        }
    }

    public static YTDStuff getYTDStuff(WSYTDDefinition wsytdDefinition, DataSet nowSet, InsightRequestMetadata insightRequestMetadata, EIConnection conn,
                                       PipelineData pipelineData, Set<AnalysisItem> reportItems) throws SQLException {
        AnalysisItem timeDimension = wsytdDefinition.getTimeDimension();
        Collection<AnalysisMeasure> measures = new ArrayList<AnalysisMeasure>();
        Collection<AnalysisMeasure> realMeasures = new ArrayList<AnalysisMeasure>();
        for (AnalysisItem analysisItem : pipelineData.getAllRequestedItems()) {
            if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                measures.add((AnalysisMeasure) analysisItem);
            }
        }
        for (AnalysisItem analysisItem : wsytdDefinition.getMeasures()) {
            realMeasures.add((AnalysisMeasure) analysisItem);
        }
        Map<AnalysisMeasure, Aggregation> ytdMap = new HashMap<AnalysisMeasure, Aggregation>();
        Map<AnalysisMeasure, Aggregation> averageMap = new HashMap<AnalysisMeasure, Aggregation>();

        for (AnalysisMeasure measure : measures) {
            AggregationFactory aggregationFactory = new AggregationFactory(measure, false);
            ytdMap.put(measure, wsytdDefinition.getFirstAggregation() == AggregationTypes.SUM ? aggregationFactory.getAggregation() :
                    aggregationFactory.getAggregation(wsytdDefinition.getFirstAggregation()));
            averageMap.put(measure, aggregationFactory.getAggregation(AggregationTypes.AVERAGE));
        }
        Set<Value> timeIntervals = new HashSet<Value>();
        Map<AnalysisMeasure, YTDValue> ytdValueMap = new HashMap<AnalysisMeasure, YTDValue>();
        for (IRow row : nowSet.getRows()) {
            Value dateValue = row.getValue(timeDimension);
            if (dateValue.type() != Value.EMPTY) {
                timeIntervals.add(dateValue);
                for (AnalysisMeasure measure : measures) {
                    Value measureValue = row.getValue(measure);
                    ytdMap.get(measure).addValue(measureValue);
                    averageMap.get(measure).addValue(measureValue);
                    YTDValue ytdValue = ytdValueMap.get(measure);
                    if (ytdValue == null) {
                        ytdValue = new YTDValue();
                        ytdValue.setAnalysisMeasure(measure);
                        ytdValueMap.put(measure, ytdValue);
                    }
                    TimeIntervalValue timeIntervalValue = new TimeIntervalValue();
                    timeIntervalValue.setDateValue(dateValue);
                    timeIntervalValue.setValue(measureValue);
                    ytdValue.getTimeIntervalValues().add(timeIntervalValue);
                }
            }
        }
        for (AnalysisMeasure measure : measures) {
            YTDValue ytdValue = ytdValueMap.get(measure);
            if (ytdValue != null) {
                if (ytdMap.get(measure) != null) {
                    ytdValue.setYtd(ytdMap.get(measure).getValue());
                }
                if (averageMap.get(measure) != null) {
                    ytdValue.setAverage(averageMap.get(measure).getValue());
                }
            }

        }
        List<IComponent> components = new CalcGraph().doFunGraphStuff(reportItems, pipelineData.getAllItems(), reportItems, true, new AnalysisItemRetrievalStructure());
        components.addAll(new CalcGraph().doFunGraphStuff(reportItems, pipelineData.getAllItems(), reportItems, false, new AnalysisItemRetrievalStructure()));
        Iterator<IComponent> iter = components.iterator();
        while (iter.hasNext()) {
            IComponent component = iter.next();
            if (!(component instanceof CalculationComponent)) {
                iter.remove();
                continue;
            }
            CalculationComponent calculationComponent = (CalculationComponent) component;
            if (calculationComponent.getAnalysisCalculation().getAggregation() != AggregationTypes.AVERAGE) {
                iter.remove();
            }
        }
        DataSet tempSet = new DataSet();
        IRow tempRow = tempSet.createRow();
        for (YTDValue copyValue : ytdValueMap.values()) {
            tempRow.addValue(copyValue.getAnalysisMeasure().createAggregateKey(), copyValue.getYtd());
        }
        for (IComponent component : components) {
            component.apply(tempSet, pipelineData);
        }
        for (YTDValue ytdValue : ytdValueMap.values()) {
            IRow tempRow1 = tempSet.getRow(0);
            AnalysisMeasure measure = ytdValue.getAnalysisMeasure();
            if (measure.hasType(AnalysisItemTypes.CALCULATION) && measure.getAggregation() == AggregationTypes.AVERAGE) {
                if (wsytdDefinition.getFirstAggregation() != AggregationTypes.MEDIAN) {
                    ytdValue.setYtd(tempRow1.getValue(measure.createAggregateKey()));
                }
                ytdValue.setAverage(tempRow1.getValue(measure.createAggregateKey()));
            }
        }
        /*for (AnalysisMeasure measure : measures) {
            if (measure.hasType(AnalysisItemTypes.CALCULATION) && measure.getAggregation() == AggregationTypes.AVERAGE) {
                
                
            }

        }*/
        List<AnalysisItem> benchmarkMeasures = new ArrayList<AnalysisItem>();
        for (AnalysisMeasure measure : measures) {
            if (measure.getReportFieldExtension() != null && measure.getReportFieldExtension() instanceof YTDReportFieldExtension) {
                YTDReportFieldExtension ytdReportFieldExtension = (YTDReportFieldExtension) measure.getReportFieldExtension();
                if (ytdReportFieldExtension.getBenchmark() != null) {
                    benchmarkMeasures.add(ytdReportFieldExtension.getBenchmark());
                }
            }
        }
        if (benchmarkMeasures.size() > 0) {
            WSListDefinition benchmarkReport = new WSListDefinition();
            benchmarkReport.setDataFeedID(wsytdDefinition.getDataFeedID());
            benchmarkReport.setColumns(benchmarkMeasures);
            benchmarkReport.setDataSourceFields(wsytdDefinition.isDataSourceFields());
            List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
            for (FilterDefinition filter : wsytdDefinition.getFilterDefinitions()) {
                if (filter instanceof FlatDateFilter) {
                    filters.add(filter);
                }
            }
            benchmarkReport.setFilterDefinitions(filters);
            DataSet benchmarkSet = DataService.listDataSet(benchmarkReport, insightRequestMetadata, conn);
            if (benchmarkSet.getRows().size() > 0) {
                IRow row = benchmarkSet.getRow(0);
                for (AnalysisMeasure measure : realMeasures) {
                    if (measure.getReportFieldExtension() != null && measure.getReportFieldExtension() instanceof YTDReportFieldExtension) {
                        YTDReportFieldExtension ytdReportFieldExtension = (YTDReportFieldExtension) measure.getReportFieldExtension();
                        if (ytdReportFieldExtension.getBenchmark() != null) {
                            AnalysisMeasure benchmarkMeasure = (AnalysisMeasure) ytdReportFieldExtension.getBenchmark();
                            YTDValue ytdValue = ytdValueMap.get(measure);
                            if (ytdValue != null) {
                                Value benchmarkValue = row.getValue(benchmarkMeasure);
                                Value average = ytdValue.getAverage();
                                double variation = (average.toDouble() - benchmarkValue.toDouble()) / benchmarkValue.toDouble() * 100;
                                ytdValue.setBenchmarkValue(benchmarkValue);
                                ytdValue.setBenchmarkMeasure(benchmarkMeasure);
                                ytdValue.setVariation(new NumericValue(variation));
                            }
                        }
                    }
                }
            }
        }
        List<YTDValue> values = new ArrayList<YTDValue>();
        for (AnalysisMeasure measure : realMeasures) {
            values.add(ytdValueMap.get(measure));
        }


        List<Value> intervals = new ArrayList<Value>(timeIntervals);
        Collections.sort(intervals, new Comparator<Value>() {

            public int compare(Value value, Value value1) {
                return value.toSortValue().compareTo(value1.toSortValue());
            }
        });
        return new YTDStuff(intervals, values);
    }
}