package com.easyinsight.outboundnotifications {

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
        var str:String = todoInfo.getTitle();
        if(str.length > 70)
            titleLabel.text = str.substr(0, 67) + "...";
        else
            titleLabel.text = str;
        titleLabel.toolTip = str;
    }

    private var todoInfo:TodoEventInfo;
    private var titleLabel:Label;

    override protected function createChildren():void {
        super.createChildren();
        this.addChild(titleLabel);
    }

    public function TodoNotifyWindowTitleLabel() {
        super();
        titleLabel = new Label();
        titleLabel.percentHeight = 100;
        titleLabel.percentWidth = 100;

    }
}
}