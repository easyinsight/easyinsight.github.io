package com.easyinsight.goals {
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
    }


    override protected function createChildren():void {
        super.createChildren();
        addChild(valueLabel);
    }

    private var _goalTreeNodeData:GoalTreeNodeData;

    override public function set data(val:Object):void {
        this._goalTreeNodeData = val as GoalTreeNodeData;
        if (this._goalTreeNodeData.currentValue != null) {
            var formatter:Formatter;
            if (_goalTreeNodeData.analysisMeasure == null) {
                formatter = new NumberFormatter();
            } else {
                formatter = _goalTreeNodeData.analysisMeasure.getFormatter();
            }
            valueLabel.text = formatter.format(_goalTreeNodeData.currentValue.value);
        }
    }

    override public function get data():Object {
        return this._goalTreeNodeData;
    }
}
}