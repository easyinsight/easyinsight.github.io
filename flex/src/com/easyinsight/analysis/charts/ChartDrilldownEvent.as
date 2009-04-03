package com.easyinsight.analysis.charts {
import flash.events.Event;
import mx.charts.ChartItem;
public class ChartDrilldownEvent extends Event{

    public static const DRILLDOWN:String = "chartDrilldown";
    public static const ROLLUP:String = "chartRollup";
    public static const VALUE_DRILLDOWN:String = "valueDrilldown";

    public var chartItem:ChartItem;
    public var value:String;

    public function ChartDrilldownEvent(type:String, chartItem:ChartItem, value:String = null) {
        super(type, true);
        this.chartItem = chartItem;
        this.value = value;
    }

    override public function clone():Event {
        return new ChartDrilldownEvent(type, chartItem);
    }
}
}