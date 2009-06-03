package com.easyinsight.notifications {
import flash.utils.describeType;

import mx.containers.HBox;
import mx.controls.Alert;
import mx.controls.Label;

public class TodoNotifyWindowTitleLabel extends HBox{

    [Bindable("dataChange")]
    [Inspectable(environment="none")]
    override public function get data():Object {
        return todoInfo;
    }

    override public function set data(value:Object):void {
        this.todoInfo = value as TodoEventInfo;
        titleLabel.text = todoInfo.getTitle();
    }

    private var todoInfo:TodoEventInfo;
    private var titleLabel:Label;

    override protected function createChildren():void {

        this.addChild(titleLabel);
        super.createChildren();
    }

    public function TodoNotifyWindowTitleLabel() {
        titleLabel = new Label();
        titleLabel.percentHeight = 100;
        titleLabel.percentWidth = 100;
        super();
    }
}
}