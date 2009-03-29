package com.easyinsight.analysis.charts {
import flash.events.Event;
import mx.charts.ChartItem;
public class ChartDrilldownEvent extends Event{

    public static const DRILLDOWN:String = "chartDrilldown";
    public static const ROLLUP:String = "chartRollup";

    public var chartItem:ChartItem;

    public function ChartDrilldownEvent(type:String, chartItem:ChartItem) {
        super(type, true);
        this.chartItem = chartItem;
    }

    override public function clone():Event {
        return new ChartDrilldownEvent(type, chartItem);
    }
}
}