package com.easyinsight.datasources {
import com.easyinsight.util.PopUpUtil;

import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Button;
import mx.managers.PopUpManager;

public class HighriseTokenControls extends HBox {

    [Embed(source="../../../../assets/pencil.png")]
    private var editIcon:Class;

    [Embed(source="../../../../assets/navigate_cross.png")]
    private var deleteIcon:Class;

    private var editButton:Button;
    private var deleteButton:Button;

    private var token:HighriseAdditionalToken;

    public function HighriseTokenControls() {
        super();
        this.percentWidth = 100;
        setStyle("horizontalAlign", "center");
    }

    protected override function createChildren():void {
        super.createChildren();
        editButton = new Button();
        editButton.setStyle("icon", editIcon);
        editButton.toolTip = "Edit this token...";
        editButton.addEventListener(MouseEvent.CLICK, onEdit);
        addChild(editButton);
        deleteButton = new Button();
        deleteButton.setStyle("icon", deleteIcon);
        deleteButton.toolTip = "Delete this token...";
        deleteButton.addEventListener(MouseEvent.CLICK, onDelete);
        addChild(deleteButton);
    }

    private function onDelete(event:MouseEvent):void {
        dispatchEvent(new HighriseTokenEvent(HighriseTokenEvent.DELETE_TOKEN, token));
    }

    private function onEdit(event:MouseEvent):void {
        var window:HighriseTokenWindow = new HighriseTokenWindow();
        window.token = token;
        PopUpManager.addPopUp(window, this, true);
        PopUpUtil.centerPopUp(window);
    }

    override public function set data(val:Object):void {
        this.token = val as HighriseAdditionalToken;
    }

    override public function get data():Object {
        return this.token;
    }
}
}