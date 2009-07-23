package com.easyinsight.goals {
import com.easyinsight.analysis.PopupMenuFactory;

import flash.events.ContextMenuEvent;
import flash.ui.ContextMenuItem;

import mx.containers.HBox;
import mx.controls.Image;
public class GoalControls extends HBox{

    [Bindable]
    [Embed(source="../../../../assets/arrow2_up_green.png")]
    private var positiveUpIcon:Class;

    [Bindable]
    [Embed(source="../../../../assets/arrow2_down_green.png")]
    private var positiveDownIcon:Class;

    [Bindable]
    [Embed(source="../../../../assets/bullet_ball_green.png")]
    private var positiveIcon:Class;

    [Bindable]
    [Embed(source="../../../../assets/arrow2_up_red.png")]
    private var negativeUpIcon:Class;

    [Bindable]
    [Embed(source="../../../../assets/arrow2_down_red.png")]
    private var negativeDownIcon:Class;

    [Bindable]
    [Embed(source="../../../../assets/bullet_square_glass_red.png")]
    private var negativeIcon:Class;

    [Bindable]
    [Embed(source="../../../../assets/bullet_ball_blue.png")]
    private var neutralIcon:Class;

    [Bindable]
    [Embed(source="../../../../assets/bullet_square_grey.png")]
    private var noDataIcon:Class;

    private var image:Image;

    public function GoalControls() {
        super();
        image = new Image();
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
        addChild(image);
    }

    private var _goalTreeNodeData:GoalTreeNodeData;

    override public function set data(val:Object):void {
        this._goalTreeNodeData = val as GoalTreeNodeData;
        var trendImage:Class;
        if (_goalTreeNodeData.goalOutcome.problemEvaluated) {
            trendImage = negativeIcon;
        } else {
            switch (_goalTreeNodeData.goalOutcome.outcomeState) {
                case GoalOutcome.EXCEEDING_GOAL:
                case GoalOutcome.POSITIVE:
                    if (_goalTreeNodeData.goalOutcome.direction == GoalOutcome.UP_DIRECTION) {
                        trendImage = positiveUpIcon;
                    } else if (_goalTreeNodeData.goalOutcome.direction == GoalOutcome.DOWN_DIRECTION) {
                        trendImage = positiveDownIcon;
                    } else {
                        trendImage = positiveIcon;
                    }
                    break;
                case GoalOutcome.NEGATIVE:
                    if (_goalTreeNodeData.goalOutcome.direction == GoalOutcome.UP_DIRECTION) {
                        trendImage = negativeDownIcon;
                    } else if (_goalTreeNodeData.goalOutcome.direction == GoalOutcome.DOWN_DIRECTION) {
                        trendImage = negativeUpIcon;
                    } else {
                        trendImage = negativeIcon;
                    }
                    break;
                case GoalOutcome.NEUTRAL:
                    trendImage = neutralIcon;
                    break;
                case GoalOutcome.NO_DATA:
                    trendImage = noDataIcon;
                    break;
            }
        }
        image.source = trendImage;
    }

    override public function get data():Object {
        return this._goalTreeNodeData;
    }
}
}