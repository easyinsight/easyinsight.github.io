package com.easyinsight.notifications {
import com.easyinsight.customupload.ConfigureDataSource;

import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Button;
import mx.managers.PopUpManager;

public class TodoNotifyWindowNavigateIcon extends HBox {

    [Bindable("dataChange")]
    [Inspectable(environment="none")]
    override public function get data():Object {
        return todoInfo;
    }

    override public function set data(value:Object):void {
        this.todoInfo = value as ConfigureDataFeedInfo;
        if(todoInfo.action == TodoEventInfo.ADD) {
            button.setStyle("icon", administerIcon);
            button.enabled = true;
        }
        else if(todoInfo.action == TodoEventInfo.COMPLETE) {
            button.setStyle("icon", checkboxIcon);
            button.enabled = false;
        }
    }

    private var todoInfo:ConfigureDataFeedInfo;
    private var button:Button;

    [Embed(source="../../../../assets/businessman_edit.png")]
    private var administerIcon:Class;

    [Embed(source="../../../../assets/check_x16.png")]
    private var checkboxIcon:Class;

    public function TodoNotifyWindowNavigateIcon() {
        button = new Button();
        button.addEventListener(MouseEvent.CLICK, onNavigateClick);
    }

    private function onNavigateClick(event:MouseEvent):void {
        var configWindow:ConfigureDataSource = new ConfigureDataSource();
        configWindow.dataSourceID = todoInfo.feedID;
        PopUpManager.addPopUp(configWindow, this.parent, true);
        PopUpManager.centerPopUp(configWindow);


    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(button);
    }
}
}