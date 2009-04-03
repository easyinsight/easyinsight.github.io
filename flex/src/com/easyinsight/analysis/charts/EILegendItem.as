package com.easyinsight.analysis.charts {
import flash.events.ContextMenuEvent;
import flash.ui.ContextMenu;
import flash.ui.ContextMenuItem;
import mx.charts.LegendItem;
public class EILegendItem extends LegendItem{
    public function EILegendItem() {
        super();
        contextMenu = new ContextMenu();
        contextMenu.hideBuiltInItems();
        var drilldownContextItem:ContextMenuItem = new ContextMenuItem("Drilldown", true);
        drilldownContextItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, onDrilldown);
        var rollupContextItem:ContextMenuItem = new ContextMenuItem("Rollup", true);
        rollupContextItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, onRollup);
        contextMenu.customItems = [ drilldownContextItem, rollupContextItem ];
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