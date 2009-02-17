package com.easyinsight.goals {
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
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(dateLabel);
    }

    private var _goalTreeNodeData:GoalTreeNodeData;

    override public function set data(val:Object):void {
        this._goalTreeNodeData = val as GoalTreeNodeData;
        if (this._goalTreeNodeData.currentValue != null) {
            var dateFormatter:DateFormatter = new DateFormatter();
            dateLabel.text = dateFormatter.format(_goalTreeNodeData.currentValue.date);
        }
    }

    override public function get data():Object {
        return this._goalTreeNodeData;
    }
}
}