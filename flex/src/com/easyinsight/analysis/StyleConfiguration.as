/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 5/25/11
 * Time: 12:36 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {

import com.easyinsight.analysis.charts.twoaxisbased.TwoAxisDefinition;
import com.easyinsight.analysis.charts.twoaxisbased.area.AreaChartDefinition;
import com.easyinsight.analysis.charts.twoaxisbased.line.LineChartDefinition;
import com.easyinsight.analysis.charts.xaxisbased.column.Column3DChartDefinition;
import com.easyinsight.analysis.charts.xaxisbased.column.ColumnChartDefinition;
import com.easyinsight.analysis.charts.xaxisbased.column.StackedColumnChartDefinition;
import com.easyinsight.analysis.charts.xaxisbased.pie.Pie3DChartDefinition;
import com.easyinsight.analysis.charts.xaxisbased.pie.PieChartDefinition;
import com.easyinsight.analysis.charts.yaxisbased.bar.Bar3DChartDefinition;
import com.easyinsight.analysis.charts.yaxisbased.bar.BarChartDefinition;
import com.easyinsight.analysis.charts.yaxisbased.bar.StackedBarChartDefinition;
import com.easyinsight.analysis.form.FormReport;
import com.easyinsight.analysis.gauge.GaugeDefinition;
import com.easyinsight.analysis.heatmap.HeatMapDefinition;
import com.easyinsight.analysis.list.ListDefinition;
import com.easyinsight.analysis.tree.TreeDefinition;
import com.easyinsight.analysis.treemap.TreeMapDefinition;
import com.easyinsight.analysis.verticallist.CombinedVerticalListDefinition;
import com.easyinsight.analysis.verticallist.VerticalListDefinition;
import com.easyinsight.analysis.ytd.CompareYearsDefinition;
import com.easyinsight.analysis.ytd.YTDDefinition;
import com.easyinsight.dashboard.Dashboard;
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
        items.addItem(new NumericReportFormItem("Padding", "padding", dashboard.padding, dashboard, 0, 100));
        items.addItem(new NumericReportFormItem("Border Thickness", "borderThickness", dashboard.borderThickness, dashboard, 0, 100));
        items.addItem(new ColorReportFormItem("Border Color", "borderColor",  dashboard.borderColor, dashboard));
        items.addItem(new ColorReportFormItem("Background Color", "backgroundColor",  dashboard.backgroundColor, dashboard));
        return items;
    }

    public static function getDashboardElementItems(dashboardElement:DashboardElement):ArrayCollection {
        var items:ArrayCollection = new ArrayCollection();
        items.addItem(new NumericReportFormItem("Preferred Width", "preferredWidth", dashboardElement.preferredWidth, dashboardElement, 0, 2000));
        items.addItem(new NumericReportFormItem("Preferred Height", "preferredHeight", dashboardElement.preferredHeight, dashboardElement, 0, 2000));
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
        if (dashboardElement is DashboardStack) {
            items.addItem(new CheckBoxReportFormItem("Consolidate Header Elements", "consolidateHeaderElements", DashboardStack(dashboardElement).consolidateHeaderElements, dashboardElement));
            items.addItem(new ComboBoxReportFormItem("Header Controls", "selectionType", DashboardStack(dashboardElement).selectionType, dashboardElement, ["Buttons", "Combo Box"]));
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
            items.addItem(new CheckBoxReportFormItem("Auto Calculate Height", "autoCalculateHeight", DashboardReport(dashboardElement).autoCalculateHeight, dashboardElement, null,
            false, function(dashboardReport:DashboardReport):Boolean {
                        return dashboardReport.report.reportType == AnalysisDefinition.LIST;
                    }));
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
        if (report.supportsEmbeddedFonts()) {
            items.addItem(new ComboBoxReportFormItem("Font Name", "fontName", report.fontName, report, ["Lucida Grande"]));
        } else {
            items.addItem(new ComboBoxReportFormItem("Font Name", "fontName", report.fontName, report, ["Arial", "Arial Black", "Comic Sans MS",
                "Courier", "Georgia", "Impact", "Monaco", "Palatino", "Tahoma", "Times New Roman", "Trebuchet MS", "Verdana"]));
        }
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
            items.addItem(new CheckBoxReportFormItem("Show Rollover Icon", "rolloverIcon", ListDefinition(report).rolloverIcon, report, null, true));
        }
        if (report is GaugeDefinition) {
            items.addItem(new NumericReportFormItem("Alert Point 1", "alertPoint1", GaugeDefinition(report).alertPoint1, report, 0, 1000000000));
            items.addItem(new NumericReportFormItem("Alert Point 2", "alertPoint2", GaugeDefinition(report).alertPoint2, report, 0, 1000000000));
            items.addItem(new ColorReportFormItem("Color 1", "color1", GaugeDefinition(report).color1, report));
            items.addItem(new ColorReportFormItem("Color 2", "color2", GaugeDefinition(report).color2, report));
            items.addItem(new ColorReportFormItem("Color 3", "color3", GaugeDefinition(report).color3, report));
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
            items.addItem(new TextReportFormItem("X Axis Label", "xAxisLabel", ChartDefinition(report).xAxisLabel, report));
            items.addItem(new TextReportFormItem("Y Axis Label", "yAxisLabel", ChartDefinition(report).yAxisLabel, report));
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
        if (report is LineChartDefinition) {
            items.addItem(new ComboBoxReportFormItem("Form", "form", TwoAxisDefinition(report).form,
                report, ["segment", "step", "reverseStep", "horizontal", "curve"]));
            items.addItem(new ComboBoxReportFormItem("Base Y Axis at Zero", "baseAtZero", TwoAxisDefinition(report).baseAtZero,
                    report, ["true", "false"]));
            items.addItem(new ComboBoxReportFormItem("Interpolate Values", "interpolateValues", TwoAxisDefinition(report).interpolateValues,
                    report, ["true", "false"]));
            items.addItem(new NumericReportFormItem("Stroke Weight", "strokeWeight", LineChartDefinition(report).strokeWeight, report, 1, 10));
        }
        if (report is AreaChartDefinition) {
            items.addItem(new ComboBoxReportFormItem("Form", "form", TwoAxisDefinition(report).form,
                report, ["segment", "step", "reverseStep", "horizontal", "curve"]));
            items.addItem(new ComboBoxReportFormItem("Base Y Axis at Zero", "baseAtZero", TwoAxisDefinition(report).baseAtZero,
                    report, ["true", "false"]));
            items.addItem(new ComboBoxReportFormItem("Interpolate Values", "interpolateValues", TwoAxisDefinition(report).interpolateValues,
                    report, ["true", "false"]));
            items.addItem(new ComboBoxReportFormItem("Stacking Type", "stackingType", AreaChartDefinition(report).stackingType,
                report, ["overlaid", "stacked", "100%"]));
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
            items.addItem(new ColorReportFormItem("Custom Chart Gradient", "gradientColor", BarChartDefinition(report).gradientColor, report));
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
            items.addItem(new ColorReportFormItem("Custom Chart Gradient", "gradientColor", ColumnChartDefinition(report).gradientColor, report));
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
        if (report is FormReport) {
            items.addItem(new ComboBoxReportFormItem("Label Font Name", "labelFont", FormReport(report).labelFont, report, ["Arial", "Arial Black", "Comic Sans MS",
                "Courier", "Georgia", "Impact", "Monaco", "Palatino", "Tahoma", "Times New Roman", "Trebuchet MS", "Verdana"]));
            items.addItem(new NumericReportFormItem("Label Font Size", "labelFontSize", FormReport(report).labelFontSize, report, 8, 48));
            items.addItem(new ComboBoxReportFormItem("Label Placement", "direction", FormReport(report).direction, report,  ["Left", "Top", "Bottom"]));
            items.addItem(new NumericReportFormItem("Number of Columns", "columnCount", FormReport(report).columnCount, report, 1, 3));
        }
        if (report is VerticalListDefinition) {
            items.addItem(new NumericReportFormItem("Header Width", "headerWidth", VerticalListDefinition(report).headerWidth, report, 100, 400));
            items.addItem(new NumericReportFormItem("Column Width", "columnWidth", VerticalListDefinition(report).columnWidth, report, 100, 400));
            items.addItem(new TextReportFormItem("Pattern Name", "patternName", VerticalListDefinition(report).patternName, report));
        }
        if (report is CompareYearsDefinition) {
            items.addItem(new TextReportFormItem("Pattern Name", "patternName", CompareYearsDefinition(report).patternName, report));
        }
        if (report is YTDDefinition) {
            items.addItem(new NumericReportFormItem("Custom Aggregation", "firstAggregation", YTDDefinition(report).firstAggregation, report, 1, 15));
        }
        if (report is CombinedVerticalListDefinition) {
            items.addItem(new NumericReportFormItem("Header Width", "headerWidth", CombinedVerticalListDefinition(report).headerWidth, report, 100, 400));
            items.addItem(new NumericReportFormItem("Column Width", "columnWidth", CombinedVerticalListDefinition(report).columnWidth, report, 100, 400));
            items.addItem(new CheckBoxReportFormItem("Hide Empty Rows", "removeEmptyRows", CombinedVerticalListDefinition(report).removeEmptyRows, report));
        }
        items.addItem(new CheckBoxReportFormItem("Optimized", "optimized", report.optimized, report));
        items.addItem(new CheckBoxReportFormItem("Filter Optimization", "lookupTableOptimization", report.lookupTableOptimization, report));
        items.addItem(new ImageReportFormItem("Header Image", "headerImage", report.headerImage, report));
        items.addItem(new CheckBoxReportFormItem("Full Joins", "fullJoins", report.fullJoins, report));
        items.addItem(new CheckBoxReportFormItem("Log Report", "logReport", report.logReport, report));
        items.addItem(new CheckBoxReportFormItem("Data Source Fields", "dataSourceFields", report.dataSourceFields, report));
        var sort:Sort = new Sort();
        sort.fields = [ new SortField("label")];
        items.sort = sort;
        items.refresh();
        return items;
    }
}
}
