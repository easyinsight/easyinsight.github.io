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
        if(str.length > 48)
            titleLabel.text = str.substr(0, 45) + "...";
        else
            titleLabel.text = str;
        titleLabel.toolTip = str;
        if(todoInfo.action == TodoEventInfo.ADD) {
            titleLabel.setStyle("textDecoration", "underline");
            titleLabel.addEventListener(MouseEvent.CLICK, onNavigateClick);
            button.visible = false;
        }
        else if(todoInfo.action == TodoEventInfo.COMPLETE) {
            titleLabel.setStyle("textDecoration", "none");
            titleLabel.removeEventListener(MouseEvent.CLICK, onNavigateClick);
            button.setStyle("icon", checkboxIcon);
            button.visible = true;
        }
    }

    private var todoInfo:TodoEventInfo;
    private var titleLabel:Label;
    private var button:Button;
    private var deleteButton:Button;

    [Embed(source="../../../../assets/check_x16.png")]
    private var checkboxIcon:Class;

    [Embed(source="../../../../assets/navigate_cross.png")]
    private var cancelIcon:Class;

    override protected function createChildren():void {
        super.createChildren();
        this.addChild(titleLabel);
        this.addChild(button);
        this.addChild(deleteButton);
    }

    public function TodoNotifyWindowRenderer() {
        super();
        titleLabel = new Label();
        titleLabel.percentHeight = 100;
        titleLabel.percentWidth = 100;
        titleLabel.setStyle("fontSize", 11);
        button = new Button();
        button.visible = false;
        deleteButton = new Button();
        deleteButton.setStyle("icon", cancelIcon);
        deleteButton.addEventListener(MouseEvent.CLICK, onDelete);
        this.setStyle("verticalAlign", "middle");
        this.width = 300;
    }

    private function onDelete(event:MouseEvent):void {
        dispatchEvent(new TodoDeleteEvent(todoInfo));
    }

    private function onNavigateClick(event:MouseEvent):void {
        var configWindow:ConfigureDataSource = new ConfigureDataSource();
        configWindow.dataSourceID = ConfigureDataFeedInfo(todoInfo).feedID;
        PopUpManager.addPopUp(configWindow, this.parent, true);
        PopUpManager.centerPopUp(configWindow);
    }

}
}