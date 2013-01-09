/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/31/12
 * Time: 4:05 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.listing {
import com.easyinsight.framework.DataFolder;
import com.easyinsight.quicksearch.EIDescriptor;
import com.easyinsight.quicksearch.EIDescriptor;
import com.easyinsight.util.RolloverLabel;

import flash.display.Bitmap;
import flash.display.BitmapData;
import flash.events.ContextMenuEvent;

import flash.events.MouseEvent;
import flash.ui.ContextMenu;
import flash.ui.ContextMenuItem;

import mx.collections.ArrayCollection;

import mx.controls.AdvancedDataGrid;
import mx.controls.Alert;

import mx.controls.Image;
import mx.controls.advancedDataGridClasses.AdvancedDataGridListData;
import mx.core.DragSource;
import mx.core.UIComponent;

import mx.events.DragEvent;
import mx.managers.DragManager;

public class ReportRolloverLabel extends RolloverLabel {
    public function ReportRolloverLabel() {
        /*addEventListener(MouseEvent.MOUSE_DOWN, onMouseDown);
        this.addEventListener(DragEvent.DRAG_ENTER, dragEnterHandler);
        this.addEventListener(DragEvent.DRAG_DROP, dragDropHandler);
        this.addEventListener(DragEvent.DRAG_OVER, dragOverHandler);
        this.addEventListener(DragEvent.DRAG_EXIT, dragExitHandler);*/

    }

    private var _folders:ArrayCollection;

    public function set folders(value:ArrayCollection):void {
        _folders = value;
        regenFolders();
    }

    override public function set data(value:Object):void {
        super.data = value;
        regenFolders();
    }

    private function regenFolders():void {
        if (_folders != null && data != null && !(data is DataFolder)) {
            var contextMenu:ContextMenu = new ContextMenu();
            var items:Array = [];
            for each (var folder:DataFolder in _folders) {
                if (folder.name == "Lookup Tables") {
                    continue;
                }
                var copyItem:ContextMenuItem = new ContextMenuItem("Move to " + folder.name);
                copyItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, createMoveFunction(EIDescriptor(data), folder));
                items.push(copyItem);

            }
            contextMenu.hideBuiltInItems();
            contextMenu.customItems = items;
            this.contextMenu = contextMenu;
        }
    }

    private function createMoveFunction(eiDesc:EIDescriptor, dataFolder:DataFolder):Function {
        return function(event:ContextMenuEvent):void {
            dispatchEvent(new ChangeFolderEvent(eiDesc, dataFolder));
        };
    }

    override protected function isLinkable(value:Object):Boolean {
        return !(value is DataFolder);
    }

    private function dragEnterHandler(event:DragEvent):void {
        var listData:AdvancedDataGridListData = listData as AdvancedDataGridListData;
        var desc:EIDescriptor = listData.item as EIDescriptor;
        if (desc.getType() == EIDescriptor.FOLDER) {
            if (desc.name != "Lookup Tables") {
                var eiDescriptor:Object = AdvancedDataGrid(ReportRolloverLabel(event.currentTarget).listData.owner).selectedItem;
                if (eiDescriptor is EIDescriptor) {
                    DragManager.acceptDragDrop(event.currentTarget as UIComponent);
                }
            }
        }
    }

    private function dragDropHandler(event:DragEvent):void {
        var listData:AdvancedDataGridListData = listData as AdvancedDataGridListData;
        var desc:EIDescriptor = listData.item as EIDescriptor;
        var eiDescriptor:EIDescriptor = AdvancedDataGrid(ReportRolloverLabel(event.currentTarget).listData.owner).selectedItem as EIDescriptor;

        dispatchEvent(new ChangeFolderEvent(eiDescriptor, desc as DataFolder));
        // changeFolder(eiDescriptor, desc.id)
    }

    private function dragOverHandler(event:DragEvent):void {
        DragManager.showFeedback(DragManager.MOVE);
    }

    private function dragExitHandler(event:DragEvent):void {

    }

    private function onMouseMove(event:MouseEvent):void {
        var listData:AdvancedDataGridListData = listData as AdvancedDataGridListData;
        var desc:EIDescriptor = listData.item as EIDescriptor;
        if (desc.getType() != EIDescriptor.FOLDER && desc.getType() != EIDescriptor.LOOKUP_TABLE) {
            var dragSource:DragSource = new DragSource();
            var bd:BitmapData = new BitmapData(this.width, this.height);
            bd.draw(this);
            var bitmap:Bitmap = new Bitmap(bd);
            var image:Image = new Image();
            image.source = bitmap;
            DragManager.doDrag(this, dragSource, event, image);
        }
    }

    private function onMouseDown(event:MouseEvent):void {
        addEventListener(MouseEvent.MOUSE_MOVE, onMouseMove);
        addEventListener(MouseEvent.MOUSE_UP, onMouseUp);
    }

    private function onMouseUp(event:MouseEvent):void {
        removeEventListener(MouseEvent.MOUSE_MOVE, onMouseMove);
        removeEventListener(MouseEvent.MOUSE_UP, onMouseUp);
    }
}
}
