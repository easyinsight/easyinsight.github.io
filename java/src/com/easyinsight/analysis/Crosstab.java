package com.easyinsight.analysis;

import com.easyinsight.core.*;
import com.easyinsight.dataset.DataSet;

import java.util.*;

/**
 * User: jamesboe
 * Date: 7/6/11
 * Time: 4:55 PM
 */
public class Crosstab {

    private Map<Intersection, Value> intersectionMap = new HashMap<Intersection, Value>();

    private List<Section> rowSections;

    private List<Section> columnSections;

    public List<Section> getRowSections() {
        return rowSections;
    }

    public List<Section> getColumnSections() {
        return columnSections;
    }

    public Value getValue(Section rowSection, Section columnSection) {
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
            Value value = row.getValue(crosstab.getMeasures().get(0));
            intersectionMap.put(new Intersection(rowSection, columnSection), value);
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
                        value2 = new StringValue("");
                    } else if (value2.type() == Value.DATE) {
                        value2 = new DateValue(new Date(0));
                    } else if (value2.type() == Value.NUMBER) {
                        value2 = new NumericValue(-1);
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

    public CrosstabValue[][] toTable(WSCrosstabDefinition crosstabDefinition) {

        List<Section> columnSections = getColumnSections();
        List<Section> rowSections = getRowSections();
        CrosstabValue[][] array = new CrosstabValue[rowSections.size() + crosstabDefinition.getColumns().size() + 2][Math.max(columnSections.size(), crosstabDefinition.getColumns().size()) + crosstabDefinition.getRows().size() + 1];
        for (int i = 0; i < crosstabDefinition.getRows().size(); i++) {
            AnalysisItem row = crosstabDefinition.getRows().get(i);
            array[1][i] = new CrosstabValue(new StringValue(row.toDisplay()), row, true, false);
        }
        for (int i = 0; i < crosstabDefinition.getColumns().size(); i++) {
            AnalysisItem column = crosstabDefinition.getColumns().get(i);
            array[0][i + crosstabDefinition.getRows().size()] = new CrosstabValue(new StringValue(column.toDisplay()), column, true, false);
        }
        for (int i = 0; i < columnSections.size(); i++) {
            Section columnSection = columnSections.get(i);
            for (int j = 0; j < crosstabDefinition.getColumns().size(); j++) {
                Value headerValue = columnSection.values.get(j);
                array[j + 1][i + crosstabDefinition.getRows().size()] = new CrosstabValue(headerValue, crosstabDefinition.getColumns().get(j));
            }
        }
        for (int j = 0; j < rowSections.size(); j++) {
            Section columnSection = rowSections.get(j);
            for (int i = 0; i < crosstabDefinition.getRows().size(); i++) {
                Value headerValue = columnSection.values.get(i);
                array[j + crosstabDefinition.getColumns().size() + 1][i] = new CrosstabValue(headerValue, crosstabDefinition.getRows().get(i));
            }
        }
        for (int i = 0; i < columnSections.size(); i++) {
            Section columnSection = columnSections.get(i);
            for (int j = 0; j < rowSections.size(); j++) {
                Section rowSection = rowSections.get(j);
                Value value = intersectionMap.get(new Intersection(rowSection, columnSection));
                if (value == null) {
                    value = new EmptyValue();
                }
                array[j + crosstabDefinition.getColumns().size() + 1][i + crosstabDefinition.getRows().size()] = new CrosstabValue(value, null);
            }
        }

        for (int i = 0; i < columnSections.size(); i++) {
            double sum = 0;
            Section columnSection = columnSections.get(i);
            AggregationFactory aggregationFactory = new AggregationFactory((AnalysisMeasure) crosstabDefinition.getMeasures().get(0), false);
            Aggregation aggregation = aggregationFactory.getAggregation();
            for (int j = 0; j < rowSections.size(); j++) {
                Section rowSection = rowSections.get(j);
                Value value = intersectionMap.get(new Intersection(rowSection, columnSection));
                if (value != null) {
                    aggregation.addValue(value);
                }
            }
            Double sumValue = aggregation.toDouble();
            if (sumValue != null) {
                sum = sumValue;
            }
            array[rowSections.size() + crosstabDefinition.getColumns().size() + 1][i + crosstabDefinition.getRows().size()] = new CrosstabValue(new NumericValue(sum), null, false, true);
        }

        for (int j = 0; j < rowSections.size(); j++) {
            Section rowSection = rowSections.get(j);
            double sum = 0;
            AggregationFactory aggregationFactory = new AggregationFactory((AnalysisMeasure) crosstabDefinition.getMeasures().get(0), false);
            Aggregation aggregation = aggregationFactory.getAggregation();
            for (int i = 0; i < columnSections.size(); i++) {
                Section columnSection = columnSections.get(i);
                Value value = intersectionMap.get(new Intersection(rowSection, columnSection));
                if (value != null) {
                    aggregation.addValue(value);
                }
            }
            Double sumValue = aggregation.toDouble();
            if (sumValue != null) {
                sum = sumValue;
            }
            array[j + crosstabDefinition.getColumns().size() + 1][columnSections.size() + crosstabDefinition.getRows().size()] = new CrosstabValue(new NumericValue(sum), null, false, true);
        }


        return array;
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
