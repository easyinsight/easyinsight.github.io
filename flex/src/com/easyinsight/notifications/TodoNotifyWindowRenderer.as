package com.easyinsight.notifications {
import com.easyinsight.customupload.ConfigureDataSource;

import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Button;
import mx.controls.Label;
import mx.managers.PopUpManager;

public class TodoNotifyWindowRenderer extends HBox {


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
        if(todoInfo.action == TodoEventInfo.ADD) {
            button.setStyle("icon", administerIcon);
            button.enabled = true;
        }
        else if(todoInfo.action == TodoEventInfo.COMPLETE) {
            button.setStyle("icon", checkboxIcon);
            button.enabled = false;
        }
    }

    private var todoInfo:TodoEventInfo;
    private var titleLabel:Label;
    private var button:Button;

    [Embed(source="../../../../assets/businessman_edit.png")]
    private var administerIcon:Class;

    [Embed(source="../../../../assets/check_x16.png")]
    private var checkboxIcon:Class;

    override protected function createChildren():void {
        super.createChildren();
        this.addChild(titleLabel);
        this.addChild(button);
    }

    public function TodoNotifyWindowRenderer() {
        super();
        titleLabel = new Label();
        titleLabel.percentHeight = 100;
        titleLabel.percentWidth = 100;
        titleLabel.setStyle("fontSize", 11);
        button = new Button();
        button.addEventListener(MouseEvent.CLICK, onNavigateClick);
        this.setStyle("verticalAlign", "middle");
        this.percentWidth=100;
    }

    private function onNavigateClick(event:MouseEvent):void {
        var configWindow:ConfigureDataSource = new ConfigureDataSource();
        configWindow.dataSourceID = ConfigureDataFeedInfo(todoInfo).feedID;
        PopUpManager.addPopUp(configWindow, this.parent, true);
        PopUpManager.centerPopUp(configWindow);
    }

}
}