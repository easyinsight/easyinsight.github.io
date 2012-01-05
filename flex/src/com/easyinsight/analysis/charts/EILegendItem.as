package com.easyinsight.analysis.charts {

import flash.events.ContextMenuEvent;
import mx.charts.LegendItem;
import mx.core.IUITextField;

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

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        labelField.setActualSize(unscaledWidth - 20, unscaledHeight);
        labelField.truncateToFit();
    }

    private var labelField:IUITextField;

    override protected function createInFontContext(classObj:Class):Object {
        labelField = IUITextField(super.createInFontContext(classObj));
        return labelField;
    }
}
}