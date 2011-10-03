/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 5/26/11
 * Time: 2:05 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.reportviews {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.ChartDefinition;
import com.easyinsight.analysis.FillProvider;
import com.easyinsight.analysis.Value;

import mx.charts.CategoryAxis;
import mx.charts.HitData;
import mx.charts.LinearAxis;
import mx.charts.chartClasses.ChartBase;
import mx.charts.chartClasses.IAxis;
import mx.charts.chartClasses.Series;
import mx.collections.ArrayCollection;
import mx.formatters.Formatter;

import spark.components.HGroup;

public class CartesianChartView extends HGroup implements IReportView {

    [Bindable]
    private var graphData:ArrayCollection = new ArrayCollection();

    [Bindable]
    private var dimensionAxisItem:AnalysisItem;

    protected var measureFormatter:Formatter;

    private var dimensionFormatter:Formatter;

    private var chart:ChartBase;

    private var dimensionAxis:CategoryAxis;
    private var measureAxis:LinearAxis;

    /*[Bindable]
    private var legend:Legend;*/

    public function CartesianChartView() {
        this.percentHeight = 100;
        this.percentWidth = 100;
    }

    protected function assignDimensionAxis(axis:CategoryAxis, chart:ChartBase):void {

    }

    protected function assignMeasureAxis(axis:LinearAxis, chart:ChartBase):void {

    }

    protected function createSeries(measure:AnalysisMeasure, dataSet:ArrayCollection, xField:String, measureNumber:int):Series {
        return null;
    }

    private function renderAxis(labelValue:Object, previousValue:Object, axis:IAxis):String {
        return measureFormatter.format(labelValue);
    }

    private function renderDimensionAxis(item:Object, prevValue:Object, axis:CategoryAxis, categoryItem:Object):String {
        var value:Value = categoryItem[dimensionAxisItem.qualifiedName()];
        return dimensionFormatter.format(value.getValue());
    }

    protected function formatDataTip(hd:HitData):String {
        return null;
    }

    protected function createChartObject():ChartBase {
        return null;
    }

    private function createChart():void {
        if (chart == null) {

            // define the chart itself

            chart = createChartObject();
            //chart.addEventListener(ChartItemEvent.ITEM_CLICK, onClick);
            chart.percentHeight = 100;
            chart.percentWidth = 100;
            chart.showDataTips = true;
            chart.cacheAsBitmap = true;
            chart.dataProvider = graphData;
            chart.dataTipFunction = formatDataTip;

            // define the X axis

            dimensionAxis = new CategoryAxis();
            dimensionAxis.labelFunction = renderDimensionAxis;
            dimensionAxis.dataProvider = graphData;
            assignDimensionAxis(dimensionAxis, chart);

            // define the Y axis

            measureAxis = new LinearAxis();
            measureAxis.title = "Blah";
            measureAxis.labelFunction = renderAxis;

            // create the data series
            addElement(chart);
            /*legend = new Legend();
            legend.direction = "vertical";
            legend.percentHeight = 100;
            legend.visible = true;
            addChild(legend);*/
        } else {
            chart.visible = true;
        }
    }

    protected function sortData(dataSet:ArrayCollection):void {
    }

    protected function getDimensionItem():AnalysisItem {
        return null;
    }

    protected function getMeasures():ArrayCollection {
        return null;
    }

    protected var chartDef:ChartDefinition;

    protected function useChartColor():Boolean {
        return false;
    }

    protected function getChartColor():uint {
        return 0;
    }

    protected function getColorScheme():String {
        return null;
    }

    protected function getAngle():int {
        return 0;
    }

    private function styleColumns(mySeries:Array):Array {
        var legendItems:Array = [];
        for (var j:int = 0; j < getMeasures().length; j++) {
            var measure:AnalysisMeasure = getMeasures().getItemAt(j) as AnalysisMeasure;
            var fills:Array;
            if (getMeasures().length == 1) {
                if (useChartColor()) {
                    fills = [getChartColor()];
                } else {
                    fills = FillProvider.getColors(getColorScheme(), [], getAngle());
                }
            } else {
                fills = [FillProvider.getColor(getColorScheme(), [], j, getAngle())];
            }
            mySeries[j].setStyle("fills", fills);

            /*var legendItem:EILegendData = new EILegendData();
            legendItem.label = measure.display;
            legendItems.push(legendItem);*/
        }
        return legendItems;
    }

    public function preserveValues():Boolean {
        return true;
    }

    public function renderReport(data:ArrayCollection, report:AnalysisDefinition, additionalProperties:Object):void {
        chartDef = report as ChartDefinition;

        if (data.length > 0) {

            var firstMeasure:AnalysisMeasure = getMeasures().getItemAt(0) as AnalysisMeasure;

            sortData(data);

            createChart();

            //applyGeneralStyling();

            measureFormatter = firstMeasure.getFormatter();

            dimensionAxisItem = getDimensionItem();

            dimensionFormatter = dimensionAxisItem.getFormatter();
            dimensionAxis.categoryField = dimensionAxisItem.qualifiedName();
            dimensionAxis.title = dimensionAxisItem.display;

            var mySeries:Array = new Array();
            for (var i:int = 0; i < getMeasures().length; i++) {
                var measure:AnalysisMeasure = getMeasures().getItemAt(i) as AnalysisMeasure;
                var series:Series = createSeries(measure, data, dimensionAxisItem.qualifiedName(), i);
                mySeries.push(series);
            }

            var max:int = 0;
            for each (var row:Object in data) {
                var value:Value = row[firstMeasure.qualifiedName()];
                var num:Number = value.toNumber();
                max = Math.max(num, max);
            }

            measureAxis.maximum = max + (max / 20);

            var legendItems:Array = styleColumns(mySeries);
            chart.series = mySeries;

            measureAxis.title = firstMeasure.display;

            assignMeasureAxis(measureAxis, chart);

            chart.dataProvider = data;
            dimensionAxis.dataProvider = data;
            //legend.dataProvider = legendItems;

            /*if (!chartDef.showLegend || getMeasures().length == 1) {
             currentState = "hideLegend";
             } else {
             currentState = "";
             }*/
        } else {
            /*if (getMeasures() != null && getMeasures().length > 0 && getDimensionItem() != null) stackIndex = 2;
             else stackIndex = 1;*/
            if (chart != null) {
                chart.dataProvider = new ArrayCollection();
                dimensionAxis.dataProvider = new ArrayCollection();
                chart.visible = false;
            }
        }

        graphData = data;
    }
}
}
