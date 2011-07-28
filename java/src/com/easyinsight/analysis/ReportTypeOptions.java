package com.easyinsight.analysis;

/**
 * User: jamesboe
 * Date: 6/29/11
 * Time: 10:41 AM
 */
public class ReportTypeOptions {

    private boolean list = true;
    private boolean tree = true;
    private boolean crosstab = true;
    private boolean columnChart = true;
    private boolean barChart = true;
    private boolean pieChart = true;
    private boolean lineChart = true;
    private boolean areaChart = true;
    private boolean plotChart = true;
    private boolean bubbleChart = true;
    private boolean gauge = true;
    private boolean treeMap = true;
    private boolean gantt = true;
    private boolean heatMap = true;
    private boolean form = true;

    public boolean isList() {
        return list;
    }

    public void setList(boolean list) {
        this.list = list;
    }

    public boolean isTree() {
        return tree;
    }

    public void setTree(boolean tree) {
        this.tree = tree;
    }

    public boolean isCrosstab() {
        return crosstab;
    }

    public void setCrosstab(boolean crosstab) {
        this.crosstab = crosstab;
    }

    public boolean isColumnChart() {
        return columnChart;
    }

    public void setColumnChart(boolean columnChart) {
        this.columnChart = columnChart;
    }

    public boolean isBarChart() {
        return barChart;
    }

    public void setBarChart(boolean barChart) {
        this.barChart = barChart;
    }

    public boolean isPieChart() {
        return pieChart;
    }

    public void setPieChart(boolean pieChart) {
        this.pieChart = pieChart;
    }

    public boolean isLineChart() {
        return lineChart;
    }

    public void setLineChart(boolean lineChart) {
        this.lineChart = lineChart;
    }

    public boolean isAreaChart() {
        return areaChart;
    }

    public void setAreaChart(boolean areaChart) {
        this.areaChart = areaChart;
    }

    public boolean isPlotChart() {
        return plotChart;
    }

    public void setPlotChart(boolean plotChart) {
        this.plotChart = plotChart;
    }

    public boolean isBubbleChart() {
        return bubbleChart;
    }

    public void setBubbleChart(boolean bubbleChart) {
        this.bubbleChart = bubbleChart;
    }

    public boolean isGauge() {
        return gauge;
    }

    public void setGauge(boolean gauge) {
        this.gauge = gauge;
    }

    public boolean isTreeMap() {
        return treeMap;
    }

    public void setTreeMap(boolean treeMap) {
        this.treeMap = treeMap;
    }

    public boolean isGantt() {
        return gantt;
    }

    public void setGantt(boolean gantt) {
        this.gantt = gantt;
    }

    public boolean isHeatMap() {
        return heatMap;
    }

    public void setHeatMap(boolean heatMap) {
        this.heatMap = heatMap;
    }

    public boolean isForm() {
        return form;
    }

    public void setForm(boolean form) {
        this.form = form;
    }
}
