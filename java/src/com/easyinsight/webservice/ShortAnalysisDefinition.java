package com.easyinsight.webservice;

/**
 * User: James Boe
 * Date: Aug 13, 2008
 * Time: 11:28:03 AM
 */
public class ShortAnalysisDefinition {
    private ShortListDefinition list;
    private ShortChartDefinition chart;
    private ShortCrosstabDefinition crosstab;

    public ShortListDefinition getList() {
        return list;
    }

    public void setList(ShortListDefinition list) {
        this.list = list;
    }

    public ShortChartDefinition getChart() {
        return chart;
    }

    public void setChart(ShortChartDefinition chart) {
        this.chart = chart;
    }

    public ShortCrosstabDefinition getCrosstab() {
        return crosstab;
    }

    public void setCrosstab(ShortCrosstabDefinition crosstab) {
        this.crosstab = crosstab;
    }
}
