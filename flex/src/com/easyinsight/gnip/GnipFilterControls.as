package com.easyinsight.gnip {
import com.easyinsight.datasources.GnipFilter;

import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Alert;
import mx.controls.Button;
import mx.events.CloseEvent;
import mx.managers.PopUpManager;

public class GnipFilterControls extends HBox {

    private var _gnipFilter:GnipFilter;

    private var editButton:Button;
    private var deleteButton:Button;

    [Embed(source="../../../../assets/pencil.png")]
    private var editIcon:Class;

    [Embed(source="../../../../assets/navigate_cross.png")]
    private var deleteIcon:Class;

    public function GnipFilterControls() {
        editButton = new Button();
        editButton.setStyle("icon", editIcon);
        editButton.toolTip = "Edit...";
        editButton.addEventListener(MouseEvent.CLICK, onEdit);

        deleteButton = new Button();
        deleteButton.setStyle("icon", deleteIcon);
        deleteButton.toolTip = "Delete";
        deleteButton.addEventListener(MouseEvent.CLICK, onDelete);

        this.setStyle("horizontalAlign", "center");
        this.percentWidth = 100;
    }

    override protected function createChildren():void {
        super.createChildren();
        this.addChild(editButton);
        this.addChild(deleteButton);
    }
                  
    private function onDelete(event:MouseEvent):void {
        Alert.show("Are you sure you want to delete this filter? You will not be able to undo this operation.", "Alert",
                Alert.OK | Alert.CANCEL, this, alertListener, null, Alert.CANCEL);
    }

    private function alertListener(event:CloseEvent):void {
        if (event.detail == Alert.OK) {
            dispatchEvent(new GnipFilterEvent(GnipFilterEvent.FILTER_DELETED, _gnipFilter));
        }
    }

    private function onEdit(event:MouseEvent):void {
        var window:GnipFilterEditor = new GnipFilterEditor();
        window.gnipFilter = this._gnipFilter;
        PopUpManager.addPopUp(window, this, true);
        PopUpManager.centerPopUp(window);
    }


    [Bindable("dataChange")]
    [Inspectable(environment="none")]

    override public function get data():Object {
        return _gnipFilter;
    }

    override public function set data(value:Object):void {
        this._gnipFilter = value as GnipFilter;
    }
}
}