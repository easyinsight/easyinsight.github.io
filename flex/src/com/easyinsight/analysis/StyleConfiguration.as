/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 5/25/11
 * Time: 12:36 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import com.easyinsight.analysis.ListLimitsFormItem;
import com.easyinsight.analysis.charts.twoaxisbased.TwoAxisDefinition;
import com.easyinsight.analysis.charts.twoaxisbased.line.LineChartDefinition;
import com.easyinsight.analysis.charts.xaxisbased.column.Column3DChartDefinition;
import com.easyinsight.analysis.charts.xaxisbased.column.ColumnChartDefinition;
import com.easyinsight.analysis.charts.xaxisbased.column.StackedColumnChartDefinition;
import com.easyinsight.analysis.charts.xaxisbased.pie.Pie3DChartDefinition;
import com.easyinsight.analysis.charts.xaxisbased.pie.PieChartDefinition;
import com.easyinsight.analysis.charts.yaxisbased.bar.Bar3DChartDefinition;
import com.easyinsight.analysis.charts.yaxisbased.bar.BarChartDefinition;
import com.easyinsight.analysis.charts.yaxisbased.bar.StackedBarChartDefinition;
import com.easyinsight.analysis.heatmap.HeatMapDefinition;
import com.easyinsight.analysis.list.ListDefinition;
import com.easyinsight.analysis.maps.MapDefinition;
import com.easyinsight.analysis.tree.TreeDefinition;
import com.easyinsight.analysis.treemap.TreeMapDefinition;
import com.easyinsight.dashboard.Dashboard;
import com.easyinsight.dashboard.DashboardElement;
import com.easyinsight.dashboard.DashboardElement;
import com.easyinsight.dashboard.DashboardGrid;
import com.easyinsight.dashboard.DashboardReport;
import com.easyinsight.dashboard.DashboardScorecard;
import com.easyinsight.dashboard.DashboardStack;

import mx.collections.ArrayCollection;
import mx.collections.Sort;
import mx.collections.SortField;

public class StyleConfiguration {
    public function StyleConfiguration() {
    }

    public static function getDashboardItems(dashboard:Dashboard):ArrayCollection {
        var items:ArrayCollection = new ArrayCollection();
        items.addItem(new NumericReportFormItem("Padding Left", "paddingLeft", dashboard.paddingLeft, dashboard, 0, 100));
        items.addItem(new NumericReportFormItem("Padding Right", "paddingRight", dashboard.paddingRight, dashboard, 0, 100));
        items.addItem(new ComboBoxReportFormItem("Filter Border Style", "filterBorderStyle", dashboard.filterBorderStyle, dashboard, ["solid", "none"]));
        items.addItem(new ColorReportFormItem("Filter Border Color", "filterBorderColor",  dashboard.filterBorderColor, dashboard));
        items.addItem(new ColorReportFormItem("Filter Background Color", "filterBackgroundColor",  dashboard.filterBackgroundColor, dashboard));
        items.addItem(new NumericReportFormItem("Filter Background Alpha", "filterBackgroundAlpha",  dashboard.filterBackgroundAlpha, dashboard, 0, 1));
        return items;
    }

    public static function getDashboardElementItems(dashboardElement:DashboardElement):ArrayCollection {
        var items:ArrayCollection = new ArrayCollection();
        if (dashboardElement is DashboardGrid || dashboardElement is DashboardStack) {
            items.addItem(new TextReportFormItem("Label", "label", dashboardElement.label, dashboardElement));
            items.addItem(new NumericReportFormItem("Padding Left", "paddingLeft", dashboardElement.paddingLeft, dashboardElement, 0, 100));
            items.addItem(new NumericReportFormItem("Padding Right", "paddingRight", dashboardElement.paddingRight, dashboardElement, 0, 100));
            items.addItem(new NumericReportFormItem("Padding Top", "paddingTop", dashboardElement.paddingTop, dashboardElement, 0, 100));
            items.addItem(new NumericReportFormItem("Padding Bottom", "paddingBottom", dashboardElement.paddingBottom, dashboardElement, 0, 100));
            items.addItem(new ComboBoxReportFormItem("Filter Border Style", "filterBorderStyle", dashboardElement.filterBorderStyle, dashboardElement, ["solid", "none"]));
            items.addItem(new ColorReportFormItem("Filter Border Color", "filterBorderColor",  dashboardElement.filterBorderColor, dashboardElement));
            items.addItem(new ColorReportFormItem("Filter Background Color", "filterBackgroundColor",  dashboardElement.filterBackgroundColor, dashboardElement));
            items.addItem(new NumericReportFormItem("Filter Background Alpha", "filterBackgroundAlpha",  dashboardElement.filterBackgroundAlpha, dashboardElement, 0, 1));
            items.addItem(new ImageReportFormItem("Header Background Image", "headerBackground",  dashboardElement.headerBackground, dashboardElement));
            items.addItem(new ColorReportFormItem("Header Background Color", "headerBackgroundColor",  dashboardElement.headerBackgroundColor, dashboardElement));
            items.addItem(new NumericReportFormItem("Header Background Alpha", "headerBackgroundAlpha",  dashboardElement.headerBackgroundAlpha, dashboardElement, 0, 1));
        }
        if (dashboardElement is DashboardGrid) {
            items.addItem(new NumericReportFormItem("Width", "width", DashboardGrid(dashboardElement).width, dashboardElement, 0, 2000));
            items.addItem(new ColorReportFormItem("Background Color", "backgroundColor",  DashboardGrid(dashboardElement).backgroundColor, dashboardElement));
            items.addItem(new NumericReportFormItem("Background Alpha", "backgroundAlpha",  DashboardGrid(dashboardElement).backgroundAlpha, dashboardElement, 0, 1));
        }
        if (dashboardElement is DashboardScorecard) {
            items.addItem(new CheckBoxReportFormItem("Show Label", "showLabel", DashboardScorecard(dashboardElement).showLabel, dashboardElement));
        }
        if (dashboardElement is DashboardReport) {
            items.addItem(new CheckBoxReportFormItem("Show Label", "showLabel", DashboardReport(dashboardElement).showLabel, dashboardElement));
        }
        var sort:Sort = new Sort();
        sort.fields = [ new SortField("label")];
        items.sort = sort;
        items.refresh();
        return items;
    }

    public static function getLimitItems(report:AnalysisDefinition, allFields:ArrayCollection = null):ArrayCollection {
        var items:ArrayCollection = new ArrayCollection();
        if (report is ListDefinition) {
            var listLimitsFormItem:ListLimitsFormItem = new ListLimitsFormItem();
            listLimitsFormItem.report = report;
            listLimitsFormItem.allFields = allFields;
            items.addItem(listLimitsFormItem);
        }
        if (report is ChartDefinition) {
            var limitsFormItem:LimitsFormItem = new LimitsFormItem();
            limitsFormItem.report = report;
            limitsFormItem.allFields = allFields;
            items.addItem(limitsFormItem);
        }
        return items;
    }

    public static function getFormItems(report:AnalysisDefinition):ArrayCollection {
        var items:ArrayCollection = new ArrayCollection();
        items.addItem(new ComboBoxReportFormItem("Font Name", "fontName", report.fontName, report, ["Arial", "Arial Black", "Comic Sans MS",
                "Courier", "Georgia", "Impact", "Monaco", "Palatino", "Tahoma", "Times New Roman", "Trebuchet MS", "Verdana"]));
        items.addItem(new NumericReportFormItem("Font Size", "fontSize", report.fontSize, report, 8, 48));
        items.addItem(new NumericReportFormItem("Background Alpha", "backgroundAlpha", report.backgroundAlpha, report, 0, 1));
        items.addItem(new NumericReportFormItem("Fixed Report Width", "fixedWidth", report.fixedWidth, report, 0, 5000));
        if (report is ListDefinition) {
            items.addItem(new CheckBoxReportFormItem("Summary Row", "summaryTotal", ListDefinition(report).summaryTotal, report, null, true));
            items.addItem(new CheckBoxReportFormItem("Show Line Numbers", "showLineNumbers", ListDefinition(report).showLineNumbers, report));
            items.addItem(new ColorReportFormItem("Text Color", "textColor", ListDefinition(report).textColor, report));
            items.addItem(new ColorReportFormItem("Header Text Color", "headerTextColor", ListDefinition(report).headerTextColor, report));
            items.addItem(new ColorReportFormItem("Alternating Row Color 1", "rowColor1", ListDefinition(report).rowColor1, report));
            items.addItem(new ColorReportFormItem("Alternating Row Color 2", "rowColor2", ListDefinition(report).rowColor2, report));
            items.addItem(new ColorReportFormItem("Header Top Color", "headerColor1", ListDefinition(report).headerColor1, report));
            items.addItem(new ColorReportFormItem("Header Bottom Color", "headerColor2", ListDefinition(report).headerColor2, report));
            items.addItem(new ColorReportFormItem("Summary Row Text Color", "summaryRowTextColor", ListDefinition(report).summaryRowTextColor, report));
            items.addItem(new ColorReportFormItem("Summary Row Background Color", "summaryRowBackgroundColor", ListDefinition(report).summaryRowBackgroundColor, report));
        }
        if (report is TreeDefinition) {
            items.addItem(new ColorReportFormItem("Text Color", "textColor", TreeDefinition(report).textColor, report));
            items.addItem(new ColorReportFormItem("Header Text Color", "headerTextColor", TreeDefinition(report).headerTextColor, report));
            items.addItem(new ColorReportFormItem("Alternating Row Color 1", "rowColor1", TreeDefinition(report).rowColor1, report));
            items.addItem(new ColorReportFormItem("Alternating Row Color 2", "rowColor2", TreeDefinition(report).rowColor2, report));
            items.addItem(new ColorReportFormItem("Header Top Color", "headerColor1", TreeDefinition(report).headerColor1, report));
            items.addItem(new ColorReportFormItem("Header Bottom Color", "headerColor2", TreeDefinition(report).headerColor2, report));
        }
        if (report is ChartDefinition) {
            items.addItem(new CheckBoxReportFormItem("Show Legend", "showLegend", ChartDefinition(report).showLegend, report));
        }
        if (report is HeatMapDefinition) {
            items.addItem(new NumericReportFormItem("Precision", "precision", HeatMapDefinition(report).precision, report, 0, 3));
        }
        if (report is TreeMapDefinition) {
            items.addItem(new ComboBoxReportFormItem("Color Strategy", "colorStrategy", TreeMapDefinition(report).colorStrategy,
                report, ["Linear", "Logarithmic"]));
            items.addItem(new ColorReportFormItem("High Color", "highColor", TreeMapDefinition(report).highColor, report));
            items.addItem(new ColorReportFormItem("Low Color", "lowColor", TreeMapDefinition(report).lowColor, report));
        }
        if (report is MapDefinition) {
            items.addItem(new ComboBoxReportFormItem("Color Strategy", "colorStrategy", MapDefinition(report).colorStrategy,
                report, ["Linear", "Logarithmic"]));
            items.addItem(new ColorReportFormItem("High Color", "highColor", MapDefinition(report).highColor, report));
            items.addItem(new ColorReportFormItem("Low Color", "lowColor", MapDefinition(report).lowColor, report));
        }
        if (report is TwoAxisDefinition) {
            items.addItem(new ComboBoxReportFormItem("Form", "form", TwoAxisDefinition(report).form,
                report, ["segment", "step", "reverseStep", "horizontal", "curve"]));
            items.addItem(new ComboBoxReportFormItem("Base Y Axis at Zero", "baseAtZero", TwoAxisDefinition(report).baseAtZero,
                    report, ["true", "false"]));
            items.addItem(new ComboBoxReportFormItem("Interpolate Values", "interpolateValues", TwoAxisDefinition(report).interpolateValues,
                    report, ["true", "false"]));
        }
        if (report is LineChartDefinition) {
            items.addItem(new NumericReportFormItem("Stroke Weight", "strokeWeight", LineChartDefinition(report).strokeWeight, report, 1, 10));
        }
        if (report is PieChartDefinition) {
            items.addItem(new ComboBoxReportFormItem("Color Scheme", "colorScheme", PieChartDefinition(report).colorScheme,
                    report, [FillProvider.radialGradients, FillProvider.highContrast]));
            items.addItem(new ComboBoxReportFormItem("Label Position", "labelPosition", PieChartDefinition(report).labelPosition,
                    report, ["callout", "insideWithCallout", "inside", "outside", "none"]));
        }
        if (report is Pie3DChartDefinition) {
            items.addItem(new ComboBoxReportFormItem("Color Scheme", "colorScheme", Pie3DChartDefinition(report).colorScheme,
                    report, [FillProvider.radialGradients, FillProvider.highContrast]));
            items.addItem(new ComboBoxReportFormItem("Label Position", "labelPosition", Pie3DChartDefinition(report).labelPosition,
                    report, ["callout", "insideWithCallout", "inside", "outside", "none"]));
        }
        if (report is Bar3DChartDefinition) {
            items.addItem(new ComboBoxReportFormItem("Color Scheme", "colorScheme", Bar3DChartDefinition(report).colorScheme,
                    report, [FillProvider.ocean, FillProvider.linearGradients, FillProvider.highContrast]));
            items.addItem(new CheckBoxReportFormItem("Use Custom Chart Color", "useChartColor", Bar3DChartDefinition(report).useChartColor, report));
            items.addItem(new ColorReportFormItem("Custom Chart Color", "chartColor", Bar3DChartDefinition(report).chartColor, report));
            items.addItem(new ComboBoxReportFormItem("Chart Sort", "columnSort", Bar3DChartDefinition(report).columnSort, report,
                [ChartDefinition.SORT_UNSORTED, ChartDefinition.SORT_X_ASCENDING, ChartDefinition.SORT_X_DESCENDING,
                ChartDefinition.SORT_Y_ASCENDING, ChartDefinition.SORT_Y_DESCENDING]));
        }
        if (report is BarChartDefinition) {
            items.addItem(new ComboBoxReportFormItem("Color Scheme", "colorScheme", BarChartDefinition(report).colorScheme,
                    report, [FillProvider.ocean, FillProvider.linearGradients, FillProvider.highContrast]));
            items.addItem(new CheckBoxReportFormItem("Use Custom Chart Color", "useChartColor", BarChartDefinition(report).useChartColor, report));
            items.addItem(new ColorReportFormItem("Custom Chart Color", "chartColor", BarChartDefinition(report).chartColor, report));
            items.addItem(new ComboBoxReportFormItem("Chart Sort", "columnSort", BarChartDefinition(report).columnSort, report,
                [ChartDefinition.SORT_UNSORTED, ChartDefinition.SORT_X_ASCENDING, ChartDefinition.SORT_X_DESCENDING,
                ChartDefinition.SORT_Y_ASCENDING, ChartDefinition.SORT_Y_DESCENDING]));
        }
        if (report is Column3DChartDefinition) {
            items.addItem(new ComboBoxReportFormItem("Color Scheme", "colorScheme", Column3DChartDefinition(report).colorScheme,
                    report, [FillProvider.ocean, FillProvider.linearGradients, FillProvider.highContrast]));
            items.addItem(new CheckBoxReportFormItem("Use Custom Chart Color", "useChartColor", Column3DChartDefinition(report).useChartColor, report));
            items.addItem(new ColorReportFormItem("Custom Chart Color", "chartColor", Column3DChartDefinition(report).chartColor, report));
            items.addItem(new ComboBoxReportFormItem("Chart Sort", "columnSort", Column3DChartDefinition(report).columnSort, report,
                    [ChartDefinition.SORT_UNSORTED, ChartDefinition.SORT_X_ASCENDING, ChartDefinition.SORT_X_DESCENDING,
                    ChartDefinition.SORT_Y_ASCENDING, ChartDefinition.SORT_Y_DESCENDING]));
        }
        if (report is ColumnChartDefinition) {
            items.addItem(new ComboBoxReportFormItem("Color Scheme", "colorScheme", ColumnChartDefinition(report).colorScheme,
                    report, [FillProvider.ocean, FillProvider.linearGradients, FillProvider.highContrast]));
            items.addItem(new CheckBoxReportFormItem("Use Custom Chart Color", "useChartColor", ColumnChartDefinition(report).useChartColor, report));
            items.addItem(new ColorReportFormItem("Custom Chart Color", "chartColor", ColumnChartDefinition(report).chartColor, report));
            items.addItem(new ComboBoxReportFormItem("Chart Sort", "columnSort", ColumnChartDefinition(report).columnSort, report,
                    [ChartDefinition.SORT_UNSORTED, ChartDefinition.SORT_X_ASCENDING, ChartDefinition.SORT_X_DESCENDING,
                    ChartDefinition.SORT_Y_ASCENDING, ChartDefinition.SORT_Y_DESCENDING]));
        }
        if (report is StackedBarChartDefinition) {
            items.addItem(new ComboBoxReportFormItem("Color Scheme", "colorScheme", StackedBarChartDefinition(report).colorScheme,
                    report, [FillProvider.ocean, FillProvider.linearGradients, FillProvider.highContrast]));
            items.addItem(new CheckBoxReportFormItem("Use Custom Chart Color", "useChartColor", StackedBarChartDefinition(report).useChartColor, report));
            items.addItem(new ColorReportFormItem("Custom Chart Color", "chartColor", StackedBarChartDefinition(report).chartColor, report));
            items.addItem(new ComboBoxReportFormItem("Chart Sort", "columnSort", StackedBarChartDefinition(report).columnSort, report,
                    [ChartDefinition.SORT_UNSORTED, ChartDefinition.SORT_X_ASCENDING, ChartDefinition.SORT_X_DESCENDING,
                    ChartDefinition.SORT_Y_ASCENDING, ChartDefinition.SORT_Y_DESCENDING]));
        }
        if (report is StackedColumnChartDefinition) {
            items.addItem(new ComboBoxReportFormItem("Color Scheme", "colorScheme", StackedColumnChartDefinition(report).colorScheme,
                    report, [FillProvider.ocean, FillProvider.linearGradients, FillProvider.highContrast]));
            items.addItem(new CheckBoxReportFormItem("Use Custom Chart Color", "useChartColor", StackedColumnChartDefinition(report).useChartColor, report));
            items.addItem(new ColorReportFormItem("Custom Chart Color", "chartColor", StackedColumnChartDefinition(report).chartColor, report));
            items.addItem(new ComboBoxReportFormItem("Chart Sort", "columnSort", StackedColumnChartDefinition(report).columnSort, report,
                    [ChartDefinition.SORT_UNSORTED, ChartDefinition.SORT_X_ASCENDING, ChartDefinition.SORT_X_DESCENDING,
                    ChartDefinition.SORT_Y_ASCENDING, ChartDefinition.SORT_Y_DESCENDING]));
        }
        var sort:Sort = new Sort();
        sort.fields = [ new SortField("label")];
        items.sort = sort;
        items.refresh();
        return items;
    }
}
}
