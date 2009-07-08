package com.easyinsight.analysis {
import com.easyinsight.goals.GoalTreeNodeData;

import flash.display.InteractiveObject;
import flash.ui.ContextMenu;

public class AirMenuFactory implements IMenuFactory {
    public function AirMenuFactory() {
    }

    public function createListMenu(copyFunction:Function, individualRowFunction:Function, drilldownFunction:Function, rollupFunction:Function,
            interactiveObject:InteractiveObject):void {
        
    }

    public function createStandardMenu(drilldownFunction:Function, rollupFunction:Function, interactiveObject:InteractiveObject):void {
    }


    public function createGoalDataMenu(goalTreeNode:GoalTreeNodeData, navigateToSubTreeFunction:Function, interactiveObject:InteractiveObject):void {
    }

    public function assignMenu(interactiveObject:InteractiveObject, menuItems:Array):void {
        var contextMenu:ContextMenu = new ContextMenu();
        contextMenu.customItems = menuItems;
        interactiveObject.contextMenu = contextMenu;
    }
}
}