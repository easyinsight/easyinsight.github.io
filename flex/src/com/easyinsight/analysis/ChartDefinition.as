package com.easyinsight.analysis {
import com.easyinsight.analysis.charts.ChartTypes;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.WSChartDefinition")]
public class ChartDefinition extends AnalysisDefinition {
    public var chartType:int;
    public var chartFamily:int;

    public var rotationAngle:Number;
    public var elevationAngle:Number;

    public var showLegend:Boolean = true;
    public var chartDefinitionID:Number;

    public var xAxisLabel:String;
    public var yAxisLabel:String;

    public static const SORT_X_DESCENDING:String = "X-Axis Descending";
    public static const SORT_X_ASCENDING:String = "X-Axis Ascending";
    public static const SORT_Y_ASCENDING:String = "Y-Axis Ascending";
    public static const SORT_Y_DESCENDING:String = "Y-Axis Descending";
    public static const SORT_UNSORTED:String = "Unsorted";

    public var limitsMetadata:LimitsMetadata;

    public function ChartDefinition() {
        chartType = getChartType();
        chartFamily = getChartFamily();
    }

    override public function fromSave(savedDef:AnalysisDefinition):void {
        super.fromSave(savedDef);
        this.chartDefinitionID = ChartDefinition(savedDef).chartDefinitionID;
    }

    public function getChartType():int {
        return 0;
    }

    public function getChartFamily():int {
        return 0;
    }

    override public function createDefaultLimits():void {
        if (this.limitsMetadata == null) {
            var limitsMetadata:LimitsMetadata = new LimitsMetadata();
            var limitNumber:int;
            switch (chartFamily) {
                case ChartTypes.COLUMN_FAMILY:
                case ChartTypes.BAR_FAMILY:
                case ChartTypes.PIE_FAMILY:
                        switch (chartType) {
                            case ChartTypes.COLUMN_2D_STACKED:
                            case ChartTypes.BAR_2D_STACKED:
                                limitNumber = 100;
                                break;
                            default:
                                limitNumber = 15;
                        }
                    break;
                case ChartTypes.PLOT_FAMILY:
                case ChartTypes.BUBBLE_FAMILY:
                    limitNumber = 100;
                    break;
                case ChartTypes.LINE_FAMILY:
                case ChartTypes.AREA_FAMILY:
                    limitNumber = 1000;
                    break;
            }
            limitsMetadata.number = limitNumber;
            limitsMetadata.limitEnabled = true;
            limitsMetadata.top = true;
            this.limitsMetadata = limitsMetadata;
        }
    }


    public function get stackedSelected():Boolean {
        var selected:Boolean = false;
        switch (chartFamily) {
            case ChartTypes.COLUMN_FAMILY:
                selected = chartType == ChartTypes.COLUMN_2D_STACKED || chartType == ChartTypes.COLUMN_3D_STACKED;
                break;
            case ChartTypes.BAR_FAMILY:
                selected = chartType == ChartTypes.BAR_2D_STACKED || chartType == ChartTypes.BAR_3D_STACKED;
                break;
        }
        return selected;
    }
}
}