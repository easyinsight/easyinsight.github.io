package com.easyinsight.analysis {
import com.easyinsight.goals.GoalTreeNodeData;

import flash.display.InteractiveObject;
import flash.events.ContextMenuEvent;
import flash.ui.ContextMenu;
import flash.ui.ContextMenuItem;

public class AirMenuFactory implements IMenuFactory {
    public function AirMenuFactory() {
    }

    public function createListMenu(copyFunction:Function, individualRowFunction:Function, drilldownFunction:Function, rollupFunction:Function,
            interactiveObject:InteractiveObject):void {
        
    }

    public function createStandardMenu(drilldownFunction:Function, rollupFunction:Function, interactiveObject:InteractiveObject):void {
    }


    public function createGoalDataMenu(goalTreeNode:GoalTreeNodeData, navigateToSubTreeFunction:Function, interactiveObject:InteractiveObject):void {
        var contextMenu:ContextMenu = new ContextMenu();        
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
        contextMenu.customItems = menuItems;
        interactiveObject.contextMenu = contextMenu;
    }
}
}