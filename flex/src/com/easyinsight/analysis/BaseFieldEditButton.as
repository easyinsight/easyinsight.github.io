package com.easyinsight.analysis
{
import com.easyinsight.AnalysisItemDeleteEvent;
import com.easyinsight.skin.ImageConstants;

import flash.events.Event;
import flash.events.MouseEvent;
import flash.geom.Point;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.controls.Alert;
import mx.controls.Button;
import mx.controls.listClasses.IListItemRenderer;
import mx.core.UIComponent;
import mx.events.FlexEvent;

import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class BaseFieldEditButton extends UIComponent implements IListItemRenderer
{
    private var analysisItemWrapper:AnalysisItemWrapper;
    private var _displayName:String;
    private var _analysisItems:ArrayCollection;
    private var button:Button;
    private var deleteButton:Button;
    private var copyButton:Button;


    public function set analysisItems(value:ArrayCollection):void {
        _analysisItems = value;
    }



    [Bindable]
    [Embed(source="../../../../assets/copy.png")]
    public var copyIcon:Class;

    private var analysisService:RemoteObject;

    public function BaseFieldEditButton()
    {
        super();
        setStyle("horizontalAlign", "center");
        this.percentWidth = 100;
    }

    private var _editVisible:Boolean;

    private var _copyVisible:Boolean;

    private var _deleteVisible:Boolean;


    [Bindable(event="editVisibleChanged")]
    public function get editVisible():Boolean {
        return _editVisible;
    }

    public function set editVisible(value:Boolean):void {
        if (_editVisible == value) return;
        _editVisible = value;
        dispatchEvent(new Event("editVisibleChanged"));
    }

    [Bindable(event="copyVisibleChanged")]
    public function get copyVisible():Boolean {
        return _copyVisible;
    }

    public function set copyVisible(value:Boolean):void {
        if (_copyVisible == value) return;
        _copyVisible = value;
        dispatchEvent(new Event("copyVisibleChanged"));
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

    override protected function createChildren():void {
        if (button == null) {
            button = new Button();
            button.setStyle("icon", ImageConstants.EDIT_ICON);
            button.toolTip = "Edit Field";
            BindingUtils.bindProperty(button, "visible", this, "editVisible");
            button.addEventListener(MouseEvent.CLICK, editItem);
        }
        addChild(button);
        if (copyButton == null) {
            copyButton = new Button();
            copyButton.setStyle("icon", copyIcon);
            copyButton.toolTip = "Copy...";
            copyButton.addEventListener(MouseEvent.CLICK, copy);
            BindingUtils.bindProperty(copyButton, "visible", this, "copyVisible");
        }
        addChild(copyButton);
        if (deleteButton == null) {
            deleteButton = new Button();
            deleteButton.setStyle("icon", ImageConstants.DELETE_ICON);
            deleteButton.toolTip = "Delete Field";
            deleteButton.addEventListener(MouseEvent.CLICK, deleteItem);
            BindingUtils.bindProperty(deleteButton, "visible", this, "deleteVisible");
        }
        addChild(deleteButton);
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        var buttonWidth:int = 40;
        var buttonHeight:int = 22;
        var padding:int = 5;
        button.move((padding), 0);
        button.setActualSize(buttonWidth, buttonHeight);
        copyButton.move((padding * 2) + (buttonWidth), 0);
        copyButton.setActualSize(buttonWidth, buttonHeight);
        deleteButton.move((padding * 3) + (buttonWidth * 2), 0);
        deleteButton.setActualSize(buttonWidth, buttonHeight);
    }

    private function editItem(event:MouseEvent):void {
        dispatchEvent(new AnalysisItemCopyEvent(AnalysisItemCopyEvent.ITEM_EDIT, analysisItemWrapper.analysisItem, analysisItemWrapper));
    }

    private function copy(event:MouseEvent):void {
        event.stopPropagation();
        analysisService = new RemoteObject();
        analysisService.destination = "analysisDefinition";
        analysisService.cloneItem.addEventListener(ResultEvent.RESULT, onCopy);
        analysisService.cloneItem.send(analysisItemWrapper.analysisItem);
    }

    private function onCopy(event:ResultEvent):void {
        var copyItem:AnalysisItem = analysisService.cloneItem.lastResult as AnalysisItem;
        var p:Point = new Point(this.x, this.y);
        var g:Point = parent.localToGlobal(p);
        dispatchEvent(new AnalysisItemCopyEvent(AnalysisItemCopyEvent.ITEM_COPY, copyItem, null, g.x, g.y));
    }

    [Bindable]
    public function get displayName():String {
        return _displayName;
    }

    public function set displayName(displayName:String):void {
        this._displayName = displayName;
    }

    private function deleteItem(event:MouseEvent):void {
        dispatchEvent(new AnalysisItemDeleteEvent(analysisItemWrapper));
    }

    private var _concreteFieldsEditable:Boolean;

    public function set concreteFieldsEditable(value:Boolean):void {
        _concreteFieldsEditable = value;
    }

    [Bindable("dataChange")]
    public function set data(value:Object):void {
        this.analysisItemWrapper = value as AnalysisItemWrapper;
        if (analysisItemWrapper.isAnalysisItem()) {
            editVisible = !analysisItemWrapper.analysisItem.concrete || _concreteFieldsEditable;
            copyVisible = true;
            deleteVisible = !analysisItemWrapper.analysisItem.concrete;
        } else {
            editVisible = false;
            copyVisible = false;
            deleteVisible = false;
        }
        this.displayName = analysisItemWrapper.displayName;
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }

    public function get data():Object {
        return this.analysisItemWrapper;
    }
}
}