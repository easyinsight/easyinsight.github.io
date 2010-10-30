package com.easyinsight.groups {
import com.easyinsight.genredata.PopularFeed;
import com.easyinsight.listing.DataFeedDescriptor;
import flash.events.ContextMenuEvent;
import flash.ui.ContextMenu;
import flash.ui.ContextMenuItem;
public class GroupDataSourceItem extends PopularFeed {

    private var _admin:Boolean = false;

    public function GroupDataSourceItem() {
        super();
    }

    override protected function createChildren():void {
        super.createChildren();
        contextMenu = new ContextMenu();
        contextMenu.hideBuiltInItems();
        if (_admin) {
            var deleteContextItem:ContextMenuItem = new ContextMenuItem("Remove Data Source From Group", true);
            deleteContextItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, deleteReport);
            contextMenu.customItems = [ deleteContextItem ];
        }
    }

    private function deleteReport(event:ContextMenuEvent):void {
        var dataSource:DataFeedDescriptor = data as DataFeedDescriptor;
        dispatchEvent(new RemoveItemFromGroupEvent(RemoveItemFromGroupEvent.REMOVE_DATA_SOURCE_FROM_GROUP, dataSource.id));
    }

    public function set admin(val:Boolean):void {
        _admin = val;
    }
}
}