package com.easyinsight.notifications {
import com.easyinsight.customupload.ConfigureDataSource;

import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Button;
import mx.controls.Label;
import mx.managers.PopUpManager;

public class ServerNotificationsWindowRenderer extends HBox {


    [Bindable("dataChange")]
    [Inspectable(environment="none")]
    override public function get data():Object {
        return broadcastInfo;
    }

    override public function set data(value:Object):void {
        this.broadcastInfo = value as BroadcastInfo;
        var str:String = broadcastInfo.message;
        if(str.length > 60)
            titleLabel.text = str.substr(0, 57) + "...";
        else
            titleLabel.text = str;
        titleLabel.toolTip = str;
    }

    private var broadcastInfo:BroadcastInfo;
    private var titleLabel:Label;
    private var deleteButton:Button;

    [Embed(source="../../../../assets/navigate_cross.png")]
    private var cancelIcon:Class;

    override protected function createChildren():void {
        super.createChildren();
        this.addChild(titleLabel);
        this.addChild(deleteButton);
    }

    public function ServerNotificationsWindowRenderer() {
        super();
        titleLabel = new Label();
        titleLabel.percentHeight = 100;
        titleLabel.percentWidth = 100;
        titleLabel.setStyle("fontSize", 11);
        deleteButton = new Button();
        deleteButton.setStyle("icon", cancelIcon);
        deleteButton.addEventListener(MouseEvent.CLICK, onDelete);
        this.setStyle("verticalAlign", "middle");
        this.percentWidth=100;
    }

    private function onDelete(event:MouseEvent):void {
        dispatchEvent(new BroadcastDeleteEvent(broadcastInfo));
    }
}
}