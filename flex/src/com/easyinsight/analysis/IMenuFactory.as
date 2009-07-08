package com.easyinsight.analysis {
import com.easyinsight.goals.GoalTreeNodeData;

import flash.display.InteractiveObject;

public interface IMenuFactory {
    function createListMenu(copyFunction:Function, individualRowFunction:Function, drilldownFunction:Function, rollupFunction:Function,
            interactiveObject:InteractiveObject):void;
    function createStandardMenu(drilldownFunction:Function, rollupFunction:Function, interactiveObject:InteractiveObject):void;
    function createGoalDataMenu(goalTreeNode:GoalTreeNodeData, navigateToSubTreeFunction:Function, interactiveObject:InteractiveObject):void;
    function assignMenu(interactiveObject:InteractiveObject, menuItems:Array):void;
}
}