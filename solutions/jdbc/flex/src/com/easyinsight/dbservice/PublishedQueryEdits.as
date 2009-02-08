package com.easyinsight.dbservice {
import flash.events.MouseEvent;
import mx.containers.HBox;
import mx.controls.Alert;
import mx.controls.Button;
import mx.events.CloseEvent;
import mx.managers.PopUpManager;
public class PublishedQueryEdits extends HBox{

    private var queryConfiguration:QueryConfiguration;

    [Bindable]
    [Embed(source="pencil.png")]
    private var editIcon:Class;

    [Bindable]
    [Embed(source="navigate_cross.png")]
    private var deleteIcon:Class;

    [Bindable]
    [Embed(source="media_play_green.png")]
    private var forceIcon:Class;

    private var editButton:Button;
    private var deleteButton:Button;
    private var forceButton:Button;

    public function PublishedQueryEdits() {
        super();
        editButton = new Button();
        editButton.setStyle("icon", editIcon);
        editButton.toolTip = "Edit...";
        editButton.addEventListener(MouseEvent.CLICK, onEdit);
        deleteButton = new Button();
        deleteButton.setStyle("icon", deleteIcon);
        deleteButton.toolTip = "Delete";
        deleteButton.addEventListener(MouseEvent.CLICK, onDelete);
        forceButton = new Button();
        forceButton.setStyle("icon", forceIcon);
        forceButton.toolTip = "Force Run...";
        forceButton.addEventListener(MouseEvent.CLICK, forceRun);
        percentWidth = 100;
        setStyle("horizontalAlign", "center");
    }

    private function forceRun(edit:MouseEvent):void {
        var runWindow:ForceRunWindow = new ForceRunWindow();
        runWindow.queryConfiguration = queryConfiguration;
        PopUpManager.addPopUp(runWindow, this.parent, true);
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(forceButton);
        addChild(editButton);
        addChild(deleteButton);
    }

    private function onEdit(event:MouseEvent):void {

    }

    private function onDelete(event:MouseEvent):void {
        Alert.show("Are you sure you want to delete this query", "Alert",
		                		Alert.OK | Alert.CANCEL, this, alertListener, null, Alert.CANCEL);
    }

    private function alertListener(event:CloseEvent):void {
        if (event.detail == Alert.OK) {
            dispatchEvent(new DeleteQueryEvent(queryConfiguration));
        }
    }

    override public function set data(val:Object):void {
        this.queryConfiguration = val as QueryConfiguration;
    }

    override public function get data():Object {
        return this.queryConfiguration;
    }
}
}