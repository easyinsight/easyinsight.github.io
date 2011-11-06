/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/28/11
 * Time: 9:53 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.listing {
import com.easyinsight.framework.DataFolder;
import com.easyinsight.quicksearch.EIDescriptor;
import com.easyinsight.quicksearch.EIDescriptor;

import flash.display.Bitmap;
import flash.display.BitmapData;

import flash.events.MouseEvent;

import mx.controls.AdvancedDataGrid;

import mx.controls.AdvancedDataGrid;

import mx.controls.Image;

import mx.controls.advancedDataGridClasses.AdvancedDataGridGroupItemRenderer;
import mx.controls.advancedDataGridClasses.AdvancedDataGridListData;
import mx.core.DragSource;
import mx.core.UIComponent;
import mx.events.DragEvent;
import mx.managers.DragManager;

public class NameRenderer extends AdvancedDataGridGroupItemRenderer {

    public function NameRenderer() {
        addEventListener(MouseEvent.MOUSE_DOWN, onMouseDown);
        this.addEventListener(DragEvent.DRAG_ENTER, dragEnterHandler);
        this.addEventListener(DragEvent.DRAG_DROP, dragDropHandler);
        this.addEventListener(DragEvent.DRAG_OVER, dragOverHandler);
        this.addEventListener(DragEvent.DRAG_EXIT, dragExitHandler);
        mouseChildren = true;
    }

    private function dragEnterHandler(event:DragEvent):void {
        var listData:AdvancedDataGridListData = listData as AdvancedDataGridListData;
        var desc:EIDescriptor = listData.item as EIDescriptor;
        if (desc.getType() == EIDescriptor.FOLDER) {
            if (desc.id == 1 || desc.id == 2) {
                var eiDescriptor:Object = AdvancedDataGrid(NameRenderer(event.currentTarget).listData.owner).selectedItem;
                if (eiDescriptor is EIDescriptor) {
                    DragManager.acceptDragDrop(event.currentTarget as UIComponent);
                }
            }
        }
    }

    private function dragDropHandler(event:DragEvent):void {
        var listData:AdvancedDataGridListData = listData as AdvancedDataGridListData;
        var desc:EIDescriptor = listData.item as EIDescriptor;
        var eiDescriptor:EIDescriptor = AdvancedDataGrid(NameRenderer(event.currentTarget).listData.owner).selectedItem as EIDescriptor;
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
