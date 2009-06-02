package com.easyinsight.analysis {
import flash.display.InteractiveObject;
import flash.events.ContextMenuEvent;
import flash.ui.ContextMenu;
import flash.ui.ContextMenuItem;

public class DefaultMenuFactory implements IMenuFactory {
    public function DefaultMenuFactory() {
    }

    public function createListMenu(copyFunction:Function, individualRowFunction:Function, drilldownFunction:Function, rollupFunction:Function,
            interactiveObject:InteractiveObject):void {
        var contextMenu:ContextMenu = new ContextMenu();
        contextMenu.hideBuiltInItems();
        var items:Array = [];
        if (drilldownFunction != null) {
            var drilldownContextItem:ContextMenuItem = new ContextMenuItem("Drilldown", true);
            drilldownContextItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, drilldownFunction);
            items.push(drilldownContextItem);
        }
        if (rollupFunction != null) {
            var rollupContextItem:ContextMenuItem = new ContextMenuItem("Rollup", true);
            rollupContextItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, rollupFunction);
            items.push(rollupContextItem);
        }
        var copyContextItem:ContextMenuItem = new ContextMenuItem("Copy Cell", true);
        copyContextItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, copyFunction);
        items.push(copyContextItem);
        var detailsItem:ContextMenuItem = new ContextMenuItem("View Individual Rows", true);
        detailsItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, individualRowFunction);
        items.push(detailsItem);
        contextMenu.customItems = items;
        interactiveObject.contextMenu = contextMenu;
    }

    public function createStandardMenu(drilldownFunction:Function, rollupFunction:Function, interactiveObject:InteractiveObject):void {
        var contextMenu:ContextMenu = new ContextMenu();
        contextMenu.hideBuiltInItems();
        var items:Array = [];
        if (drilldownFunction != null) {
            var drilldownContextItem:ContextMenuItem = new ContextMenuItem("Drilldown", true);
            drilldownContextItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, drilldownFunction);
            items.push(drilldownContextItem);
        }
        if (rollupFunction != null) {
            var rollupContextItem:ContextMenuItem = new ContextMenuItem("Rollup", true);
            rollupContextItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, rollupFunction);
            items.push(rollupContextItem);
        }
        interactiveObject.contextMenu = contextMenu;
    }
}
}