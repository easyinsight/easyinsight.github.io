/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 6/28/11
 * Time: 3:42 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.ReportTypeOptions")]
public class ReportTypeOptions {

    public var list:Boolean;
    public var tree:Boolean;
    public var crosstab:Boolean;
    public var columnChart:Boolean;
    public var barChart:Boolean;
    public var pieChart:Boolean;
    public var lineChart:Boolean;
    public var areaChart:Boolean;
    public var plotChart:Boolean;
    public var bubbleChart:Boolean;
    public var gauge:Boolean;
    public var treeMap:Boolean;
    public var gantt:Boolean;
    public var heatMap:Boolean;
    public var form:Boolean;

    public function ReportTypeOptions() {
    }

    public function hasType(type:int):Boolean {
        switch (type) {
            case AnalysisDefinition.AREA:
                return areaChart;
                break;
            case AnalysisDefinition.BAR:
                return barChart;
                break;
            case AnalysisDefinition.COLUMN:
                return columnChart;
                break;
            case AnalysisDefinition.CROSSTAB:
                return crosstab;
                break;
            case AnalysisDefinition.LINE:
                return lineChart;
                break;
            case AnalysisDefinition.FORM:
                return form;
                break;
            case AnalysisDefinition.GAUGE:
                return gauge;
                break;
            case AnalysisDefinition.GANTT:
                return gantt;
                break;
            case AnalysisDefinition.HEATMAP:
                return heatMap;
                break;
            case AnalysisDefinition.LIST:
                return list;
                break;
            case AnalysisDefinition.PIE:
                return pieChart;
                break;
            case AnalysisDefinition.PLOT:
                return plotChart;
                break;
            case AnalysisDefinition.BUBBLE:
                return bubbleChart;
                break;
            case AnalysisDefinition.TREE:
                return tree;
                break;
            case AnalysisDefinition.TREEMAP:
                return treeMap;
                break;
        }
        return true;
    }
}
}
