package com.easyinsight.analysis {
import com.easyinsight.util.PopUpUtil;

import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Button;
import mx.managers.PopUpManager;

public class LinkControls extends HBox{

    [Bindable]
    [Embed(source="../../../../assets/pencil.png")]
    private var editIcon:Class;

    [Bindable]
    [Embed(source="../../../../assets/navigate_cross.png")]
    private var deleteIcon:Class;

    private var editButton:Button;
    private var deleteButton:Button;

    private var link:Link;

    private var _sourceItem:AnalysisItem;

    public function LinkControls() {
        super();
        setStyle("horizontalAlign", "center");
        this.percentWidth = 100;
    }

    public function set sourceItem(value:AnalysisItem):void {
        _sourceItem = value;
    }

    override public function set data(val:Object):void {
        link = val as Link;
    }

    override public function get data():Object {
        return link;
    }

    override protected function createChildren():void {
        super.createChildren();
        if (editButton == null) {
            editButton = new Button();
            editButton.setStyle("icon", editIcon);
            editButton.toolTip = "Edit...";
            editButton.addEventListener(MouseEvent.CLICK, onEdit);
        }
        addChild(editButton);
        if (deleteButton == null) {
            deleteButton = new Button();
            deleteButton.setStyle("icon", deleteIcon);
            deleteButton.toolTip = "Delete";
            deleteButton.addEventListener(MouseEvent.CLICK, onDelete);
        }
        addChild(deleteButton);
    }

    private function onEdit(event:MouseEvent):void {
        var window:LinkWindow = new LinkWindow();
        window.link = link;
        window.sourceItem = _sourceItem;
        window.addEventListener(LinkMetadataEvent.LINK_EDITED, passThrough, false, 0, true);
        PopUpManager.addPopUp(window, this, true);
        PopUpUtil.centerPopUp(window);
    }

    private function passThrough(event:LinkMetadataEvent):void {
        dispatchEvent(event);
    }

    private function onDelete(event:MouseEvent):void {
        dispatchEvent(new LinkMetadataEvent(LinkMetadataEvent.LINK_DELETED, link));
    }
}
}