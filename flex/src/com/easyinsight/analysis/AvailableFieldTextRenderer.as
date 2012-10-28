package com.easyinsight.analysis {
import com.easyinsight.util.PopUpUtil;

import flash.events.ContextMenuEvent;
import flash.geom.Point;
import flash.ui.ContextMenu;
import flash.ui.ContextMenuItem;

import mx.controls.Alert;

import mx.controls.listClasses.IListItemRenderer;
import mx.core.Application;
import mx.core.UITextField;
import mx.managers.PopUpManager;

public class AvailableFieldTextRenderer extends UITextField implements IListItemRenderer {

    private var wrapper:AnalysisItemWrapper;
    private var _dataSourceID:int;
    private var _calcRefactor:Boolean;

    public function AvailableFieldTextRenderer() {
        super();
    }


    public function validateProperties():void {
    }

    public function validateDisplayList():void {
    }

    public function validateSize(recursive:Boolean = false):void {
    }

    public function set dataSourceID(value:int):void {
        _dataSourceID = value;
    }

    public function set calcRefactor(value:Boolean):void {
        _calcRefactor = value;
    }

    public function get data():Object {
        return wrapper;
    }

    public function set data(value:Object):void {
        wrapper = value as AnalysisItemWrapper;

        if (wrapper.analysisItem != null) {
            var items:Array = [];
            var addToReportItem:ContextMenuItem = new ContextMenuItem("Add to Report");
            addToReportItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, function(event:ContextMenuEvent):void {
                dispatchEvent(new ReportEditorFieldEvent(ReportEditorFieldEvent.ITEM_ADD_TO_REPORT, wrapper));
            });
            items.push(addToReportItem);
            var filterItem:ContextMenuItem = new ContextMenuItem("Filter on this Field");
            filterItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, function(event:ContextMenuEvent):void {
                var appHeight:int = Application(Application.application).height;

                var point:Point = new Point(this.x, this.y);
                var global:Point = localToGlobal(point);
                var margin:int = appHeight - global.y;
                var targY:int = global.y;
                if (margin < 250) {
                    targY -= (250 - margin);
                }
                dispatchEvent(new ReportEditorFieldEvent(ReportEditorFieldEvent.ITEM_FILTER, wrapper, global.x, targY));
            });
            items.push(filterItem);
            var copyItem:ContextMenuItem = new ContextMenuItem("Copy Field", true);
            copyItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, function(event:ContextMenuEvent):void {
                var p:Point = new Point(this.x, this.y);
                var g:Point = parent.localToGlobal(p);
                dispatchEvent(new ReportEditorFieldEvent(ReportEditorFieldEvent.ITEM_COPY, wrapper, g.x, g.y));
            });
            items.push(copyItem);
            if (!wrapper.analysisItem.concrete) {
                var editItem:ContextMenuItem = new ContextMenuItem("Edit Field");
                editItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, function(event:ContextMenuEvent):void {
                    var p:Point = new Point(this.x, this.y);
                    var g:Point = parent.localToGlobal(p);
                    dispatchEvent(new ReportEditorFieldEvent(ReportEditorFieldEvent.ITEM_EDIT, wrapper, g.x, g.y));
                });
                items.push(editItem);
                var deleteItem:ContextMenuItem = new ContextMenuItem("Delete Field");
                deleteItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, function(event:ContextMenuEvent):void {
                    dispatchEvent(new ReportEditorFieldEvent(ReportEditorFieldEvent.ITEM_DELETE, wrapper));
                });
                items.push(deleteItem);
                /*var refactorItem:ContextMenuItem = new ContextMenuItem("Promote to Data Source Field");
                refactorItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, function(event:ContextMenuEvent):void {
                    dispatchEvent(new ReportEditorFieldEvent(ReportEditorFieldEvent.ITEM_REFACTOR_TO_DATA_SOURCE, wrapper));
                });
                items.push(refactorItem);*/
            }
            if (_calcRefactor && wrapper.analysisItem.hasType(AnalysisItemTypes.CALCULATION)) {
                var calculateItem:ContextMenuItem = new ContextMenuItem("Convert to Calculation");
                var thisObj:AvailableFieldTextRenderer = this;
                calculateItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, function(event:ContextMenuEvent):void {
                    var window:CalculationConvertWindow = new CalculationConvertWindow();
                    window.dataSourceID = _dataSourceID;
                    window.field = wrapper.analysisItem;
                    PopUpManager.addPopUp(window, thisObj, true);
                    PopUpUtil.centerPopUp(window);
                });
                items.push(calculateItem);
            }

            PopupMenuFactory.assignMenu(this, items);
        }
    }
}
}