package com.easyinsight.goals {
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
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(image);
    }

    private var _goalTreeNodeData:GoalTreeNodeData;

    override public function set data(val:Object):void {
        this._goalTreeNodeData = val as GoalTreeNodeData;
        if (_goalTreeNodeData.coreFeedID == 0) {
            switch (_goalTreeNodeData.goalOutcome.outcomeState) {
                case GoalOutcome.EXCEEDING_GOAL:
                case GoalOutcome.POSITIVE:
                    image.source = positiveIcon;
                    break;
                case GoalOutcome.NEGATIVE:
                    image.source = negativeIcon;
                    break;
                case GoalOutcome.NEUTRAL:
                    image.source = neutralIcon;
                    break;
                case GoalOutcome.NO_DATA:
                    image.source = noDataIcon;
                    break;
            }
        } else {
            switch (_goalTreeNodeData.goalOutcome.outcomeState) {
                case GoalOutcome.EXCEEDING_GOAL:
                case GoalOutcome.POSITIVE:
                    image.source = _goalTreeNodeData.highIsGood ? positiveUpIcon : positiveDownIcon;
                    break;
                case GoalOutcome.NEGATIVE:
                    image.source = _goalTreeNodeData.highIsGood ? negativeDownIcon : negativeUpIcon;
                    break;
                case GoalOutcome.NEUTRAL:
                    image.source = neutralIcon;
                    break;
                case GoalOutcome.NO_DATA:
                    image.source = noDataIcon;
                    break;
            }
        }
        if (_goalTreeNodeData.goalOutcome is ConcreteGoalOutcome) {
            var concreteGoalOutcome:ConcreteGoalOutcome = _goalTreeNodeData as ConcreteGoalOutcome;
            image.toolTip = concreteGoalOutcome.startValue + " to " + concreteGoalOutcome.endValue + " (" +
                           concreteGoalOutcome.percentChange + "%)";
        }
    }

    override public function get data():Object {
        return this._goalTreeNodeData;
    }
}
}