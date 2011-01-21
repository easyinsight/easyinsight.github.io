package com.easyinsight.groups
{
import com.easyinsight.listing.*;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;
import mx.containers.HBox;
import mx.controls.Button;

public class GroupAdminMyDataIconControls extends HBox
{
    private var obj:Object;

    [Embed(source="../../../../assets/navigate_cross.png")]
    public var deleteIcon:Class;

    private var deleteButton:Button;

    private var _deleteTooltip:String = "Delete...";
    private var _deleteVisible:Boolean = true;

    public function GroupAdminMyDataIconControls()
    {
        super();
        deleteButton = new Button();
        deleteButton.setStyle("icon", deleteIcon);
        BindingUtils.bindProperty(deleteButton, "toolTip", this, "deleteTooltip");
        BindingUtils.bindProperty(deleteButton, "visible", this, "deleteVisible");
        deleteButton.addEventListener(MouseEvent.CLICK, deleteCalled);
        addChild(deleteButton);

        this.setStyle("paddingLeft", 5);
        this.setStyle("paddingRight", 5);
    }

    [Bindable(event="deleteTooltipChanged")]
    public function get deleteTooltip():String {
        return _deleteTooltip;
    }

    public function set deleteTooltip(value:String):void {
        if (_deleteTooltip == value) return;
        _deleteTooltip = value;
        dispatchEvent(new Event("deleteTooltipChanged"));
    }

    [Bindable(event="deleteVisibleChanged")]
    public function get deleteVisible():Boolean {
        return _deleteVisible;
    }

    public function set deleteVisible(value:Boolean):void {
        if (_deleteVisible == value) return;
        _deleteVisible = value;
        dispatchEvent(new Event("deleteVisibleChanged"));
    }

    private function deleteCalled(event:MouseEvent):void {
        dispatchEvent(new DeleteDataSourceEvent(obj));
    }

    override public function set data(value:Object):void {
        this.obj = value;
        deleteVisible = true;
    }

    override public function get data():Object {
        return this.obj;
    }
}
}