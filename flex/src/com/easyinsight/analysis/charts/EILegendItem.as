package com.easyinsight.analysis.charts {
import com.easyinsight.analysis.PopupMenuFactory;

import flash.events.ContextMenuEvent;
import mx.charts.LegendItem;
public class EILegendItem extends LegendItem{
    public function EILegendItem() {
        super();
        PopupMenuFactory.menuFactory.createStandardMenu(onDrilldown, onRollup, this);
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