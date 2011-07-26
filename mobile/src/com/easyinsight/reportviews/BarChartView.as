/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 6/13/11
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.reportviews {
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.charts.yaxisbased.bar.BarChartDefinition;

import mx.charts.BarChart;

import mx.charts.CategoryAxis;

import mx.charts.ChartItem;
import mx.charts.HitData;
import mx.charts.LinearAxis;
import mx.charts.chartClasses.CartesianChart;
import mx.charts.chartClasses.CartesianTransform;
import mx.charts.chartClasses.ChartBase;
import mx.charts.chartClasses.DataTransform;
import mx.charts.chartClasses.Series;
import mx.charts.series.BarSeries;
import mx.charts.series.items.BarSeriesItem;
import mx.collections.ArrayCollection;

public class BarChartView extends CartesianChartView {
    public function BarChartView() {
    }

    override protected function getDimensionItem():AnalysisItem {
            return BarChartDefinition(chartDef).yaxis;
        }

        override protected function getMeasures():ArrayCollection {
            return BarChartDefinition(chartDef).measures;
        }

        override protected function createChartObject():ChartBase {
            return new BarChart();
        }

        override protected function useChartColor():Boolean {
            return BarChartDefinition(chartDef).useChartColor;
        }

        override protected function getChartColor():uint {
            return BarChartDefinition(chartDef).chartColor;
        }

        override protected function getColorScheme():String {
            return BarChartDefinition(chartDef).colorScheme;
        }

        override protected function sortData(dataSet:ArrayCollection):void {
            /*var columnChartDef:BarChartDefinition = chartDef as BarChartDefinition;
            var firstMeasure:AnalysisMeasure = columnChartDef.measures.getItemAt(0) as AnalysisMeasure;
            if (columnChartDef.columnSort != ChartDefinition.SORT_UNSORTED) {
                var sort:Sort = new Sort();
                if (columnChartDef.columnSort == ChartDefinition.SORT_X_ASCENDING) {
                    sort.compareFunction = SortFunctionFactory.createSortFunction(getDimensionItem(), false);
                } else if (columnChartDef.columnSort == ChartDefinition.SORT_X_DESCENDING) {
                    sort.compareFunction = SortFunctionFactory.createSortFunction(getDimensionItem(), true);
                } else if (columnChartDef.columnSort == ChartDefinition.SORT_Y_ASCENDING) {
                    sort.compareFunction = SortFunctionFactory.createSortFunction(firstMeasure, false);
                } else if (columnChartDef.columnSort == ChartDefinition.SORT_Y_DESCENDING) {
                    sort.compareFunction = SortFunctionFactory.createSortFunction(firstMeasure, true);
                }
                dataSet.sort = sort;
                dataSet.refresh();
            }*/
        }


        override protected function getAngle():int {
            return 90;
        }

        override protected function formatDataTip(hd:HitData):String {
            var dt:String = "";
            var barSeriesItem:BarSeriesItem = hd.chartItem as BarSeriesItem;
            var series:Series = barSeriesItem.element as Series;
            var dataTransform:DataTransform = series.dataTransform;
            var n:String = series.displayName;
            if (n != null && n.length > 0)
                dt += "<b>" + n + "</b><BR/>";

            var xName:String = dataTransform.getAxis(CartesianTransform.VERTICAL_AXIS).displayName;
            if (xName != "")
                dt += "<i>" + xName + ":</i> ";
            dt += dataTransform.getAxis(CartesianTransform.VERTICAL_AXIS).formatForScreen(BarSeriesItem(hd.chartItem).yValue) + "\n";

            var yName:String = dataTransform.getAxis(CartesianTransform.HORIZONTAL_AXIS).displayName;

            if (yName != "")
                dt += "<i>" + yName + ":</i> ";
            dt += measureFormatter.format(BarSeriesItem(hd.chartItem).xValue) + "\n";

            return dt;
        }

        override protected function assignDimensionAxis(axis:CategoryAxis, chart:ChartBase):void {
            CartesianChart(chart).verticalAxis = axis;
        }

        override protected function assignMeasureAxis(axis:LinearAxis, chart:ChartBase):void {
            CartesianChart(chart).horizontalAxis = axis;
        }

        override protected function createSeries(measure:AnalysisMeasure, dataSet:ArrayCollection, xField:String, measureNumber:int):Series {
            var barSeries:BarSeries = new BarSeries();
            barSeries.yField = xField;
            barSeries.xField = measure.qualifiedName();
            barSeries.labelFunction = function (element:ChartItem, series:Series):String {
                var barSeriesItem:BarSeriesItem = element as BarSeriesItem;
                return measure.getFormatter().format(barSeriesItem.xNumber);
            };
            barSeries.dataProvider = dataSet;
            barSeries.dataFunction = function(series:Series, item:Object, fieldName:String):Object {
                if (fieldName == 'xValue')
                    return(item[measure.qualifiedName()].toNumber());
                else if (fieldName == "yValue")
                    return(item[xField].toString());
                else
                    return null;
            };
            barSeries.displayName = measure.display;
            return barSeries;
        }
}
}
