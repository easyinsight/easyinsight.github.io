package com.easyinsight.analysis;

import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;
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
                Value value2 = section1.values.get(i);
                if (!value1.toString().equals(value2.toString())) {
                    return value1.toString().compareTo(value2.toString());
                }
            }
            return 0;
        }
    }

    public CrosstabValue[][] toTable(WSCrosstabDefinition crosstabDefinition) {

        List<Section> columnSections = getColumnSections();
        List<Section> rowSections = getRowSections();
        CrosstabValue[][] array = new CrosstabValue[rowSections.size() + crosstabDefinition.getColumns().size()][columnSections.size() + crosstabDefinition.getRows().size()];
        for (int i = 0; i < columnSections.size(); i++) {
            Section columnSection = columnSections.get(i);
            for (int j = 0; j < crosstabDefinition.getColumns().size(); j++) {
                Value headerValue = columnSection.values.get(j);
                array[j][i + crosstabDefinition.getRows().size()] = new CrosstabValue(headerValue, crosstabDefinition.getColumns().get(j));
            }
        }
        for (int j = 0; j < rowSections.size(); j++) {
            Section columnSection = rowSections.get(j);
            for (int i = 0; i < crosstabDefinition.getRows().size(); i++) {
                Value headerValue = columnSection.values.get(i);
                array[j + crosstabDefinition.getColumns().size()][i] = new CrosstabValue(headerValue, crosstabDefinition.getRows().get(i));
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
                array[j + crosstabDefinition.getColumns().size()][i + crosstabDefinition.getRows().size()] = new CrosstabValue(value, null);
            }
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
