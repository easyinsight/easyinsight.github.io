package com.easyinsight.goals {
import com.easyinsight.analysis.PopupMenuFactory;

import flash.events.ContextMenuEvent;
import flash.ui.ContextMenuItem;

import mx.containers.HBox;
import mx.controls.Label;
import mx.formatters.Formatter;
import mx.formatters.NumberFormatter;
public class GoalValueRenderer extends HBox{

    private var valueLabel:Label;

    public function GoalValueRenderer() {
        super();
        valueLabel = new Label();
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
        addChild(valueLabel);
    }

    private var _goalTreeNodeData:GoalTreeNodeData;

    override public function set data(val:Object):void {
        this._goalTreeNodeData = val as GoalTreeNodeData;
        if (this._goalTreeNodeData.goalOutcome != null) {
            var formatter:Formatter;
            if (_goalTreeNodeData.analysisMeasure == null) {
                formatter = new NumberFormatter();
            } else {
                formatter = _goalTreeNodeData.analysisMeasure.getFormatter();
            }
            valueLabel.text = formatter.format(_goalTreeNodeData.goalOutcome.outcomeValue);
        }
    }

    override public function get data():Object {
        return this._goalTreeNodeData;
    }
}
}