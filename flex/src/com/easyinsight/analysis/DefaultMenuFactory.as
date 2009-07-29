package com.easyinsight.analysis {
import com.easyinsight.goals.GoalTreeNodeData;

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
        contextMenu.customItems = items;
        interactiveObject.contextMenu = contextMenu;
    }

    public function createGoalDataMenu(goalTreeNode:GoalTreeNodeData, navigateToSubTreeFunction:Function, interactiveObject:InteractiveObject):void {
        var contextMenu:ContextMenu = new ContextMenu();
        contextMenu.hideBuiltInItems();
        var items:Array = [];
        if (goalTreeNode.subTreeID > 0) {
            var subTreeItem:ContextMenuItem = new ContextMenuItem("Navigate to Sub Tree", true);
            subTreeItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, navigateToSubTreeFunction);
            items.push(subTreeItem);
        }
        contextMenu.customItems = items;
        interactiveObject.contextMenu = contextMenu;
    }

    public function assignMenu(interactiveObject:InteractiveObject, menuItems:Array):void {
        var contextMenu:ContextMenu = new ContextMenu();
        contextMenu.hideBuiltInItems();
        contextMenu.customItems = menuItems;
        interactiveObject.contextMenu = contextMenu;
    }
}
}