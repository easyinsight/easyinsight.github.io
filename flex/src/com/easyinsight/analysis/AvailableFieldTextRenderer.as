package com.easyinsight.analysis {
import flash.events.ContextMenuEvent;
import flash.geom.Point;
import flash.ui.ContextMenuItem;

import mx.controls.listClasses.IListItemRenderer;
import mx.core.UITextField;

public class AvailableFieldTextRenderer extends UITextField implements IListItemRenderer {

    private var wrapper:AnalysisItemWrapper;

    public function AvailableFieldTextRenderer() {
        super();
    }


    public function validateProperties():void {
    }

    public function validateDisplayList():void {
    }

    public function validateSize(recursive:Boolean = false):void {
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
                var point:Point = new Point(this.x, this.y);
                var global:Point = localToGlobal(point);
                dispatchEvent(new ReportEditorFieldEvent(ReportEditorFieldEvent.ITEM_FILTER, wrapper, global.x, global.y));
            });
            items.push(filterItem);
            var copyItem:ContextMenuItem = new ContextMenuItem("Copy Field", true);
            copyItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, function(event:ContextMenuEvent):void {
                dispatchEvent(new ReportEditorFieldEvent(ReportEditorFieldEvent.ITEM_COPY, wrapper));
            });
            items.push(copyItem);
            if (!wrapper.analysisItem.concrete) {
                var editItem:ContextMenuItem = new ContextMenuItem("Edit Field");
                editItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, function(event:ContextMenuEvent):void {
                    dispatchEvent(new ReportEditorFieldEvent(ReportEditorFieldEvent.ITEM_EDIT, wrapper));
                });
                items.push(editItem);
                var deleteItem:ContextMenuItem = new ContextMenuItem("Delete Field");
                deleteItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, function(event:ContextMenuEvent):void {
                    dispatchEvent(new ReportEditorFieldEvent(ReportEditorFieldEvent.ITEM_DELETE, wrapper));
                });
                items.push(deleteItem);
            }
            PopupMenuFactory.assignMenu(this, items);
        }
    }
}
}