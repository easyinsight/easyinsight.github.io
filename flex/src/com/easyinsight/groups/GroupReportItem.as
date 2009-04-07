package com.easyinsight.groups {
import com.easyinsight.genredata.PopularAnalysisItem;
import com.easyinsight.solutions.InsightDescriptor;
import flash.events.ContextMenuEvent;
import flash.ui.ContextMenu;
import flash.ui.ContextMenuItem;
public class GroupReportItem extends PopularAnalysisItem {

    private var _admin:Boolean = false;

    public function GroupReportItem() {
        super();
    }

    override protected function createChildren():void {
        super.createChildren();
        contextMenu = new ContextMenu();
        contextMenu.hideBuiltInItems();
        if (_admin) {
            var deleteContextItem:ContextMenuItem = new ContextMenuItem("Remove Report From Group", true);
            deleteContextItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, deleteReport);
            contextMenu.customItems = [ deleteContextItem ];
        }
    }

    private function deleteReport(event:ContextMenuEvent):void {
        var insightDescriptor:InsightDescriptor = data as InsightDescriptor;
        dispatchEvent(new RemoveItemFromGroupEvent(RemoveItemFromGroupEvent.REMOVE_REPORT_FROM_GROUP, insightDescriptor.id));
    }

    public function set admin(val:Boolean):void {
        _admin = val;
    }
}
}