package com.easyinsight.goals {
import com.easyinsight.analysis.PopupMenuFactory;

import flash.events.ContextMenuEvent;
import flash.ui.ContextMenuItem;

import mx.controls.Label;

public class GoalNameRenderer extends Label{
    public function GoalNameRenderer() {
        super();
        var removeItem:ContextMenuItem = new ContextMenuItem("Remove Goal");
        removeItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, onRemove);
        PopupMenuFactory.assignMenu(this, [ removeItem]);
    }

    private function onRemove(event:ContextMenuEvent):void {
        dispatchEvent(new GoalSubscriptionEvent(GoalSubscriptionEvent.GOAL_REMOVE, _goalTreeNodeData.goalTreeNodeID));
    }

    private var _goalTreeNodeData:GoalTreeNodeData;

    override public function set data(val:Object):void {
        this._goalTreeNodeData = val as GoalTreeNodeData;
        this.text = _goalTreeNodeData.name;
    }

    override public function get data():Object {
        return this._goalTreeNodeData;
    }
}
}