package com.easyinsight.analysis.charts {

import flash.events.ContextMenuEvent;
import mx.charts.LegendItem;
public class EILegendItem extends LegendItem{
    public function EILegendItem() {
        super();
		invalidateDisplayList();
    }

    private function onDrilldown(event:ContextMenuEvent):void {
        dispatchEvent(new ChartDrilldownEvent(ChartDrilldownEvent.VALUE_DRILLDOWN, null, label));
    }

    private function onRollup(event:ContextMenuEvent):void {
        dispatchEvent(new ChartDrilldownEvent(ChartDrilldownEvent.ROLLUP, null));
    }

    [Inspectable(environment="none")]
    override public function set legendData(value:Object):void {
        super.legendData = value;

    }
}
}