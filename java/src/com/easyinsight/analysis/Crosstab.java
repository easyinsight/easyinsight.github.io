package com.easyinsight.analysis;

import com.easyinsight.core.*;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;

import java.util.*;
import java.util.stream.Collectors;

/**
 * User: jamesboe
 * Date: 7/6/11
 * Time: 4:55 PM
 */
public class Crosstab {

    private Map<Intersection, Map<AnalysisItem, Value>> intersectionMap = new HashMap<Intersection, Map<AnalysisItem, Value>>();

    private List<Section> rowSections;

    private List<Section> columnSections;

    public List<Section> getRowSections() {
        return rowSections;
    }

    public List<Section> getColumnSections() {
        return columnSections;
    }

    public Map<AnalysisItem, Value> getValue(Section rowSection, Section columnSection) {
        return intersectionMap.get(new Intersection(rowSection, columnSection));
    }

    public void crosstab(WSCrosstabDefinition crosstab, DataSet dataSet) {
        Set<Section> rowSectionSet = new HashSet<Section>();
        Set<Section> columnSectionSet = new HashSet<Section>();
        for (IRow row : dataSet.getRows()) {
            Section rowSection = new Section();
            Section columnSection = new Section();
            for (AnalysisItem rowItem : crosstab.getRows()) {
                rowSection.values.add(row.getValue(rowItem));
            }
            for (AnalysisItem columnItem : crosstab.getColumns()) {
                columnSection.values.add(row.getValue(columnItem));
            }
            rowSectionSet.add(rowSection);
            columnSectionSet.add(columnSection);
            Intersection intersection = new Intersection(rowSection, columnSection);
            Map<AnalysisItem, Value> metricMap = intersectionMap.get(intersection);
            if (metricMap == null) {
                metricMap = new HashMap<AnalysisItem, Value>();
                intersectionMap.put(intersection, metricMap);
            }
            for (AnalysisItem analysisItem : crosstab.getMeasures()) {
                metricMap.put(analysisItem, row.getValue(analysisItem));
            }
        }
        rowSections = new ArrayList<Section>(rowSectionSet);
        columnSections = new ArrayList<Section>(columnSectionSet);
        Collections.sort(rowSections, new SectionComparator(crosstab.getRows().size()));
        Collections.sort(columnSections, new SectionComparator(crosstab.getColumns().size()));
    }

    private static class SectionComparator implements Comparator<Section> {

        private int levels;

        private SectionComparator(int levels) {
            this.levels = levels;
        }

        public int compare(Section section, Section section1) {
            for (int i = 0; i < levels; i++) {
                Value value1 = section.values.get(i);
                if (value1.getSortValue() != null) {
                    value1 = value1.getSortValue();
                }
                Value value2 = section1.values.get(i);
                if (value2.getSortValue() != null) {
                    value2 = value2.getSortValue();
                }
                if (value1.type() == Value.EMPTY) {
                    if (value2.type() == Value.STRING) {
                        value1 = new StringValue("");
                    } else if (value2.type() == Value.DATE) {
                        value1 = new DateValue(new Date(0));
                    } else if (value2.type() == Value.NUMBER) {
                        value1 = new NumericValue(-1);
                    }
                } else if (value2.type() == Value.EMPTY) {
                    if (value1.type() == Value.STRING) {
                        value2 = new StringValue("");
                    } else if (value1.type() == Value.DATE) {
                        value2 = new DateValue(new Date(0));
                    } else if (value1.type() == Value.NUMBER) {
                        value2 = new NumericValue(-1);
                    }
                }
                if (value1.type() == Value.STRING && value2.type() == Value.STRING) {
                    if (!value1.toString().equals(value2.toString())) {
                        return value1.toString().compareTo(value2.toString());
                    }
                } else if (value1.type() == Value.DATE && value2.type() == Value.DATE) {
                    DateValue dateValue1 = (DateValue) value1;
                    DateValue dateValue2 = (DateValue) value2;
                    return dateValue1.getDate().compareTo(dateValue2.getDate());
                } else if (value1.type() == Value.NUMBER && value2.type() == Value.NUMBER) {
                    return value1.toDouble().compareTo(value2.toDouble());
                }
            }
            return 0;
        }
    }

    public CrosstabValue[][] toTable(WSCrosstabDefinition crosstabDefinition, InsightRequestMetadata insightRequestMetadata, EIConnection conn) {
        Map<Value, Value> rowSet = null;
        Map<Value, Value> columnSet = null;
        
        AnalysisMeasure baseMeasure = (AnalysisMeasure) crosstabDefinition.getMeasures().get(0);


        List<Section> columnSections = getColumnSections();
        List<Section> rowSections = getRowSections();
        int rowOffset = crosstabDefinition.getMeasures().size() > 1 ? 2 : 1;

        if (crosstabDefinition.getMaxColumns() > 0 && crosstabDefinition.getMeasures().size() == 1 && columnSections.size() > crosstabDefinition.getMaxColumns()) {
            applyColumnLimits(crosstabDefinition, columnSections, rowSections);
        }

        if (crosstabDefinition.getMaxRows() > 0 && crosstabDefinition.getMeasures().size() == 1 && rowSections.size() > crosstabDefinition.getMaxRows()) {
            applyRowLimits(crosstabDefinition, columnSections, rowSections);
        }

        if (crosstabDefinition.getMeasures().size() == 1 && crosstabDefinition.isSortByRowSummaries()) {
            AnalysisMeasure analysisMeasure = (AnalysisMeasure) crosstabDefinition.getMeasures().get(0);
            Map<Section, Double> lookupMap = new HashMap<>();
            for (Section rowSection : rowSections) {
                Map<AnalysisItem, Aggregation> sumMap = new HashMap<>();
                sumMap.put(analysisMeasure, new AggregationFactory(analysisMeasure, false).getAggregation());
                for (Section columnSection : columnSections) {
                    Map<AnalysisItem, Value> map = intersectionMap.get(new Intersection(rowSection, columnSection));
                    if (map != null) {
                        Value value = map.get(analysisMeasure);
                        if (value != null) {
                            sumMap.get(analysisMeasure).addValue(value);
                        }
                    }
                }
                double sum = 0;
                Double sumValue = sumMap.get(analysisMeasure).toDouble();
                if (sumValue != null) {
                    sum = sumValue;
                }
                lookupMap.put(rowSection, sum);
            }
            List<Section> sorted = new ArrayList<>(rowSections);
            Collections.sort(sorted, (o1, o2) -> lookupMap.get(o2).compareTo(lookupMap.get(o1)));
            rowSections = sorted;
        }

        if (baseMeasure.getAggregation() == AggregationTypes.AVERAGE && crosstabDefinition.getColumns().size() == 1 && crosstabDefinition.getRows().size() == 1 &&
                crosstabDefinition.getMeasures().size() == 1) {

            WSListDefinition rowTotals = new WSListDefinition();
            rowTotals.setAddedItems(crosstabDefinition.getAddedItems());
            rowTotals.setAddonReports(crosstabDefinition.getAddonReports());
            rowTotals.setDataFeedID(crosstabDefinition.getDataFeedID());
            rowTotals.setOptimized(crosstabDefinition.isOptimized());
            rowTotals.setJoinOverrides(crosstabDefinition.getJoinOverrides());
            rowTotals.setFilterDefinitions(crosstabDefinition.getFilterDefinitions());
            rowTotals.setColumns(Arrays.asList(crosstabDefinition.getMeasures().get(0), crosstabDefinition.getRows().get(0)));
            DataSet rowDataSet = DataService.listDataSet(rowTotals, insightRequestMetadata, conn);

            rowSet = new HashMap<>();
            for (IRow row : rowDataSet.getRows()) {
                rowSet.put(row.getValue(crosstabDefinition.getRows().get(0)), row.getValue(crosstabDefinition.getMeasures().get(0)));
            }

            WSListDefinition columnTotals = new WSListDefinition();
            columnTotals.setAddedItems(crosstabDefinition.getAddedItems());
            columnTotals.setAddonReports(crosstabDefinition.getAddonReports());
            columnTotals.setDataFeedID(crosstabDefinition.getDataFeedID());
            columnTotals.setOptimized(crosstabDefinition.isOptimized());
            columnTotals.setJoinOverrides(crosstabDefinition.getJoinOverrides());
            columnTotals.setFilterDefinitions(crosstabDefinition.getFilterDefinitions());
            columnTotals.setColumns(Arrays.asList(crosstabDefinition.getMeasures().get(0), crosstabDefinition.getColumns().get(0)));
            DataSet columnDataSet = DataService.listDataSet(columnTotals, insightRequestMetadata, conn);

            columnSet = new HashMap<>();
            for (IRow row : columnDataSet.getRows()) {
                columnSet.put(row.getValue(crosstabDefinition.getColumns().get(0)), row.getValue(crosstabDefinition.getMeasures().get(0)));
            }
        }

        CrosstabValue[][] array = new CrosstabValue[rowSections.size() + crosstabDefinition.getColumns().size() + rowOffset + 1][Math.max(columnSections.size() * crosstabDefinition.getMeasures().size(), crosstabDefinition.getColumns().size()) + crosstabDefinition.getRows().size() + 1];

        for (int i = 0; i < crosstabDefinition.getColumns().size(); i++) {
            AnalysisItem column = crosstabDefinition.getColumns().get(i);
            array[0][i + crosstabDefinition.getRows().size()] = new CrosstabValue(new StringValue(column.toDisplay()), column, true, false);
        }
        for (int i = 0; i < crosstabDefinition.getRows().size(); i++) {
            AnalysisItem row = crosstabDefinition.getRows().get(i);
            array[1][i] = new CrosstabValue(new StringValue(row.toDisplay()), row, true, false);
        }

        for (int i = 0; i < (columnSections.size() * crosstabDefinition.getMeasures().size()); i += (crosstabDefinition.getMeasures().size())) {
            Section columnSection = columnSections.get(i / crosstabDefinition.getMeasures().size());
            for (int j = 0; j < crosstabDefinition.getColumns().size(); j++) {
                Value headerValue = columnSection.values.get(j);
                array[j + 1][i + crosstabDefinition.getRows().size()] = new CrosstabValue(headerValue, crosstabDefinition.getColumns().get(j));
            }
        }
        if (crosstabDefinition.getMeasures().size() > 1) {
            for (int i = 0; i < (columnSections.size() * crosstabDefinition.getMeasures().size()); i += (crosstabDefinition.getMeasures().size())) {
                for (int j = 0; j < crosstabDefinition.getColumns().size(); j++) {
                    for (int k = 0; k < crosstabDefinition.getMeasures().size(); k++) {
                        AnalysisItem analysisItem = crosstabDefinition.getMeasures().get(k);
                        array[j + 2][i + crosstabDefinition.getRows().size() + k] = new CrosstabValue(new StringValue(analysisItem.toDisplay()), analysisItem, true, false, analysisItem);
                    }
                }
            }    
        }
        for (int j = 0; j < rowSections.size(); j++) {
            Section columnSection = rowSections.get(j);
            for (int i = 0; i < crosstabDefinition.getRows().size(); i++) {
                Value headerValue = columnSection.values.get(i);
                array[j + crosstabDefinition.getColumns().size() + rowOffset][i] = new CrosstabValue(headerValue, crosstabDefinition.getRows().get(i));
            }
        }
        for (int i = 0; i < (columnSections.size() * crosstabDefinition.getMeasures().size()); i += (crosstabDefinition.getMeasures().size())) {
            Section columnSection = columnSections.get(i / crosstabDefinition.getMeasures().size());
            for (int j = 0; j < rowSections.size(); j++) {
                Section rowSection = rowSections.get(j);
                Map<AnalysisItem, Value> map = intersectionMap.get(new Intersection(rowSection, columnSection));
                for (int k = 0; k < crosstabDefinition.getMeasures().size(); k++) {
                    AnalysisItem analysisItem = crosstabDefinition.getMeasures().get(k);
                    Value value = null;
                    if (map != null) {
                        value = map.get(analysisItem);
                    }
                    if (value == null) {
                        value = new EmptyValue();
                    }
                    CrosstabValue cv = new CrosstabValue(value, null, analysisItem);
                    array[j + crosstabDefinition.getColumns().size() + rowOffset][i + crosstabDefinition.getRows().size() + k] = cv;
                    if (analysisItem.getLinks() != null && analysisItem.getLinks().size() > 0 && crosstabDefinition.getColumns().size() == 1 && crosstabDefinition.getRows().size() == 1) {
                        cv.addDTValue(crosstabDefinition.getColumns().get(0).qualifiedName(), columnSection.values.get(0));
                        cv.addDTValue(crosstabDefinition.getRows().get(0).qualifiedName(), rowSection.values.get(0));
                    }
                }
            }
        }

        for (int i = 0; i < columnSections.size(); i++) {
            Map<AnalysisItem, Aggregation> sumMap = new HashMap<>();
            for (AnalysisItem analysisItem : crosstabDefinition.getMeasures()) {
                AnalysisMeasure analysisMeasure = (AnalysisMeasure) analysisItem;
                sumMap.put(analysisMeasure, new AggregationFactory(analysisMeasure, false).getAggregation());
            }
            Section columnSection = columnSections.get(i);
            if (columnSet == null) {
                for (int j = 0; j < crosstabDefinition.getMeasures().size(); j++) {
                    AnalysisMeasure analysisMeasure = (AnalysisMeasure) crosstabDefinition.getMeasures().get(j);

                    for (Section rowSection : rowSections) {
                        Map<AnalysisItem, Value> map = intersectionMap.get(new Intersection(rowSection, columnSection));
                        if (map != null) {
                            Value value = map.get(analysisMeasure);
                            if (value != null) {
                                sumMap.get(analysisMeasure).addValue(value);
                            }
                        }
                    }
                    double sum = 0;
                    Double sumValue = sumMap.get(analysisMeasure).toDouble();
                    if (sumValue != null) {
                        sum = sumValue;
                    }
                    CrosstabValue cv = new CrosstabValue(new NumericValue(sum), null, false, true, analysisMeasure);
                    array[rowSections.size() + crosstabDefinition.getColumns().size() + rowOffset][(i * crosstabDefinition.getMeasures().size()) + j + crosstabDefinition.getRows().size()] = cv;
                    Section rowSection = rowSections.get(j);
                    Map<AnalysisItem, Value> map = intersectionMap.get(new Intersection(rowSection, columnSection));
                    //cv.addDTValue();
                }
            } else {
                Value sectionValue = columnSection.values.get(0);
                Value aggregateValue = columnSet.get(sectionValue);
                if (aggregateValue != null) {
                    double sum = 0;
                    if (aggregateValue.toDouble() != null) {
                        sum = aggregateValue.toDouble();
                    }
                    array[rowSections.size() + crosstabDefinition.getColumns().size() + 1][i + crosstabDefinition.getRows().size()] = new CrosstabValue(new NumericValue(sum), null, false, true, crosstabDefinition.getMeasures().get(0));
                }
            }
        }

        if (crosstabDefinition.getMeasures().size() == 1) {
            AggregationFactory totalAggregationFactory = new AggregationFactory((AnalysisMeasure) crosstabDefinition.getMeasures().get(0), false);
            Aggregation totalAggregation = totalAggregationFactory.getAggregation();
            for (int j = 0; j < rowSections.size(); j++) {
                Section rowSection = rowSections.get(j);
                double sum = 0;
                if (rowSet == null) {
                    AggregationFactory aggregationFactory = new AggregationFactory((AnalysisMeasure) crosstabDefinition.getMeasures().get(0), false);
                    Aggregation aggregation = aggregationFactory.getAggregation();
                    for (int i = 0; i < columnSections.size(); i++) {
                        Section columnSection = columnSections.get(i);
                        Map<AnalysisItem, Value> map = intersectionMap.get(new Intersection(rowSection, columnSection));
                        if (map != null) {
                            Value value = map.values().iterator().next();
                            if (value != null) {
                                aggregation.addValue(value);
                            }
                        }
                    }
                    Double sumValue = aggregation.toDouble();
                    if (sumValue != null) {
                        sum = sumValue;
                    }
                } else {
                    Value sectionValue = rowSection.values.get(0);
                    Value aggregateValue = rowSet.get(sectionValue);
                    if (aggregateValue != null) {
                        if (aggregateValue.toDouble() != null) {
                            sum = aggregateValue.toDouble();
                        }
                    }
                }
                array[j + crosstabDefinition.getColumns().size() + rowOffset][columnSections.size() + crosstabDefinition.getRows().size()] = new CrosstabValue(new NumericValue(sum), null, false, true, crosstabDefinition.getMeasures().get(0));
                totalAggregation.addValue(new NumericValue(sum));
            }
            array[rowSections.size() + rowOffset + crosstabDefinition.getColumns().size()][columnSections.size() + crosstabDefinition.getRows().size()] = new CrosstabValue(totalAggregation.getValue(), null, false, true, crosstabDefinition.getMeasures().get(0));
        }


        return array;
    }

    protected void applyColumnLimits(WSCrosstabDefinition crosstabDefinition, List<Section> columnSections, List<Section> rowSections) {
        AnalysisMeasure analysisMeasure = (AnalysisMeasure) crosstabDefinition.getMeasures().get(0);
        Map<Section, Double> lookupMap = new HashMap<>();
        for (Section columnSection : columnSections) {
            Map<AnalysisItem, Aggregation> sumMap = new HashMap<>();
            sumMap.put(analysisMeasure, new AggregationFactory(analysisMeasure, false).getAggregation());
            for (Section rowSection : rowSections) {
                Map<AnalysisItem, Value> map = intersectionMap.get(new Intersection(rowSection, columnSection));
                if (map != null) {
                    Value value = map.get(analysisMeasure);
                    if (value != null) {
                        sumMap.get(analysisMeasure).addValue(value);
                    }
                }
            }
            double sum = 0;
            Double sumValue = sumMap.get(analysisMeasure).toDouble();
            if (sumValue != null) {
                sum = sumValue;
            }
            lookupMap.put(columnSection, sum);
        }
        List<Section> sorted = new ArrayList<>(columnSections);
        Collections.sort(sorted, (o1, o2) -> lookupMap.get(o2).compareTo(lookupMap.get(o1)));
        List<Section> remainder = sorted.subList(0, crosstabDefinition.getMaxColumns());
        List<Section> other = sorted.subList(crosstabDefinition.getMaxColumns(), sorted.size());

        AggregationFactory factory = new AggregationFactory((AnalysisMeasure) crosstabDefinition.getMeasures().get(0), false);
        Map<Section, Aggregation> otherAggregations = new HashMap<>();
        for (Section columnSection : other) {
            for (Section rowSection : rowSections) {
                Map<AnalysisItem, Value> map = intersectionMap.get(new Intersection(rowSection, columnSection));
                if (map != null) {
                    Value value = map.get(analysisMeasure);
                    if (value != null) {
                        Aggregation aggregation = otherAggregations.get(rowSection);
                        if (aggregation == null) {
                            aggregation = factory.getAggregation();
                            otherAggregations.put(rowSection, aggregation);
                        }
                        aggregation.addValue(value);
                    }
                }
            }
        }

        Section otherSection = new Section();
        List<Value> endValues = new ArrayList<>();
        endValues.add(new StringValue("Other"));
        otherSection.values = endValues;

        Iterator<Section> iter = columnSections.iterator();
        while (iter.hasNext()) {
            Section section = iter.next();
            if (!remainder.contains(section)) {
                iter.remove();
            }
        }
        columnSections.add(otherSection);

        for (Section row : rowSections) {
            Aggregation aggregation = otherAggregations.get(row);
            if (aggregation != null) {
                Map<AnalysisItem, Value> map = new HashMap<>();
                map.put(analysisMeasure, aggregation.getValue());
                intersectionMap.put(new Intersection(row, otherSection), map);
            }
        }
    }

    protected void applyRowLimits(WSCrosstabDefinition crosstabDefinition, List<Section> columnSections, List<Section> rowSections) {
        AnalysisMeasure analysisMeasure = (AnalysisMeasure) crosstabDefinition.getMeasures().get(0);
        Map<Section, Double> lookupMap = new HashMap<>();
        for (Section rowSection : rowSections) {
            Map<AnalysisItem, Aggregation> sumMap = new HashMap<>();
            sumMap.put(analysisMeasure, new AggregationFactory(analysisMeasure, false).getAggregation());
            for (Section columnSection : columnSections) {
                Map<AnalysisItem, Value> map = intersectionMap.get(new Intersection(rowSection, columnSection));
                if (map != null) {
                    Value value = map.get(analysisMeasure);
                    if (value != null) {
                        sumMap.get(analysisMeasure).addValue(value);
                    }
                }
            }
            double sum = 0;
            Double sumValue = sumMap.get(analysisMeasure).toDouble();
            if (sumValue != null) {
                sum = sumValue;
            }
            lookupMap.put(rowSection, sum);
        }
        List<Section> sorted = new ArrayList<>(rowSections);
        Collections.sort(sorted, (o1, o2) -> lookupMap.get(o2).compareTo(lookupMap.get(o1)));
        List<Section> remainder = sorted.subList(0, crosstabDefinition.getMaxRows());
        List<Section> other = sorted.subList(crosstabDefinition.getMaxRows(), sorted.size());

        AggregationFactory factory = new AggregationFactory((AnalysisMeasure) crosstabDefinition.getMeasures().get(0), false);
        Map<Section, Aggregation> otherAggregations = new HashMap<>();
        for (Section rowSection : other) {
            for (Section columnSection : columnSections) {
                Map<AnalysisItem, Value> map = intersectionMap.get(new Intersection(rowSection, columnSection));
                if (map != null) {
                    Value value = map.get(analysisMeasure);
                    if (value != null) {
                        Aggregation aggregation = otherAggregations.get(rowSection);
                        if (aggregation == null) {
                            aggregation = factory.getAggregation();
                            otherAggregations.put(columnSection, aggregation);
                        }
                        aggregation.addValue(value);
                    }
                }
            }
        }

        Section otherSection = new Section();
        List<Value> endValues = new ArrayList<>();
        endValues.add(new StringValue("Other"));
        otherSection.values = endValues;

        Iterator<Section> iter = rowSections.iterator();
        while (iter.hasNext()) {
            Section section = iter.next();
            if (!remainder.contains(section)) {
                iter.remove();
            }
        }
        rowSections.add(otherSection);

        for (Section column : columnSections) {
            Aggregation aggregation = otherAggregations.get(column);
            if (aggregation != null) {
                Map<AnalysisItem, Value> map = new HashMap<>();
                map.put(analysisMeasure, aggregation.getValue());
                intersectionMap.put(new Intersection(otherSection, column), map);
            }
        }
    }

    private static class Intersection {
        private Section row;
        private Section column;

        private Intersection(Section row, Section column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Intersection that = (Intersection) o;

            if (!column.equals(that.column)) return false;
            if (!row.equals(that.row)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = row.hashCode();
            result = 31 * result + column.hashCode();
            return result;
        }
    }

    private static class Section {
        private List<Value> values = new ArrayList<Value>();

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Section section = (Section) o;

            return values.equals(section.values);

        }

        @Override
        public int hashCode() {
            return values.hashCode();
        }

        @Override
        public String toString() {
            return values.toString();
        }
    }
}
