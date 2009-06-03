package com.easyinsight.notifications {
import mx.containers.HBox;
import mx.controls.Label;

public class TodoNotifyWindowTitleLabel extends HBox{

    [Bindable("dataChange")]
    [Inspectable(environment="none")]
    override public function get data():Object {
        return todoInfo;
    }

    override public function set data(value:Object):void {
        this.todoInfo = value as TodoEventInfo;
    }

    private var todoInfo:TodoEventInfo;

    override protected function createChildren():void {
        var titleLabel:Label = new Label();
        titleLabel.text = todoInfo.getTitle();
        this.addChild(titleLabel);
        super.createChildren();
    }

    public function TodoNotifyWindowTitleLabel() {
        super();
    }
}
}