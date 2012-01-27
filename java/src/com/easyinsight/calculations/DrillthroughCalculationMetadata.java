package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.FilterDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 1/26/12
 * Time: 2:57 PM
 */
public class DrillthroughCalculationMetadata extends CalculationMetadata {
    private List<FilterDefinition> drillThroughFilters = new ArrayList<FilterDefinition>();
    private List<AnalysisItem> analysisItems;
    private Map<String, Object> data;

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public List<AnalysisItem> getAnalysisItems() {
        return analysisItems;
    }

    public void setAnalysisItems(List<AnalysisItem> analysisItems) {
        this.analysisItems = analysisItems;
    }

    public List<FilterDefinition> getDrillThroughFilters() {
        return drillThroughFilters;
    }

    public void setDrillThroughFilters(List<FilterDefinition> drillThroughFilters) {
        this.drillThroughFilters = drillThroughFilters;
    }
}
