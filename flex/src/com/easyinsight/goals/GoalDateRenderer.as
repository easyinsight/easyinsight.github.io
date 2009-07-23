package com.easyinsight.goals {
import com.easyinsight.analysis.PopupMenuFactory;

import flash.events.ContextMenuEvent;
import flash.ui.ContextMenuItem;

import mx.containers.HBox;
import mx.controls.Label;
import mx.formatters.DateFormatter;
public class GoalDateRenderer extends HBox{

    private var dateLabel:Label;

    public function GoalDateRenderer() {
        super();
        dateLabel = new Label();
        setStyle("horizontalAlign", "center");
        this.percentWidth = 100;
        var removeItem:ContextMenuItem = new ContextMenuItem("Remove Goal");
        removeItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, onRemove);
        PopupMenuFactory.assignMenu(this, [ removeItem]);
    }

    private function onRemove(event:ContextMenuEvent):void {
        dispatchEvent(new GoalSubscriptionEvent(GoalSubscriptionEvent.GOAL_REMOVE, _goalTreeNodeData.goalTreeNodeID));
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(dateLabel);
    }

    private var _goalTreeNodeData:GoalTreeNodeData;

    override public function set data(val:Object):void {
        this._goalTreeNodeData = val as GoalTreeNodeData;
        if (this._goalTreeNodeData.goalOutcome != null) {
            var dateFormatter:DateFormatter = new DateFormatter();
            dateLabel.text = dateFormatter.format(_goalTreeNodeData.goalOutcome.evaluationDate);
        }
    }

    override public function get data():Object {
        return this._goalTreeNodeData;
    }
}
}