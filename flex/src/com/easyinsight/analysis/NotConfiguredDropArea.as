/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/28/11
 * Time: 11:10 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import flash.display.Shape;

import mx.containers.Canvas;
import mx.containers.HBox;
import mx.controls.Label;
import mx.core.UIComponent;
import mx.events.DragEvent;
import mx.managers.DragManager;

public class NotConfiguredDropArea extends Canvas {
    
    private var message:String;
    
    private var dropArea:DropArea;
    
    private var field:AnalysisItem;
    
    public function NotConfiguredDropArea(message:String, dropArea:DropArea) {
        super();
        this.message = message;
        this.dropArea = dropArea;
        addEventListener(DragEvent.DRAG_ENTER, dragEnterHandler);
        addEventListener(DragEvent.DRAG_DROP, dragDropHandler);
        addEventListener(DragEvent.DRAG_OVER, dragOverHandler);
        this.width = 400;
        this.height = 50;
    }

    override protected function createChildren():void {
        super.createChildren();
        var label:Label = new Label();
        label.setStyle("fontSize", 16);
        label.setStyle("textAlign", "center");
        label.percentWidth = 100;
        label.text = message;
        addChild(label);
    }

    protected function dragEnterHandler(event:DragEvent):void {
        dropArea.dragEnterHandler(event);
    }
    
    protected function dragDropHandler(event:DragEvent):void {
        dropArea.dragDropHandler(event);
    }

    protected static function dragOverHandler(event:DragEvent):void {
        DragManager.showFeedback(DragManager.MOVE);
    }

    private var borderFill:UIComponent;

    private var defaultBorderFill:UIComponent;

    private var dragComp:UIComponent;

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        if (highlighted) {

            if (!this.dragComp) {
                var screen:Shape = new Shape();
                screen.graphics.beginFill(0x00FF00, .1);
                screen.graphics.drawRect(0, 0, this.width, this.height);
                screen.graphics.endFill();
                dragComp = new UIComponent();
                dragComp.mouseChildren = false;
                dragComp.mouseEnabled = false;
                dragComp.addChild(screen);
            }

            if (!this.dragComp.parent) {
                addChild(dragComp);
            }

            if (!this.borderFill) {
                var borderShape:Shape = new Shape();
                borderShape.graphics.lineStyle(2, 0x00AA00, 1);
                borderShape.graphics.moveTo(0, 0);
                borderShape.graphics.lineTo(unscaledWidth, 0);
                borderShape.graphics.lineTo(unscaledWidth, unscaledHeight);
                borderShape.graphics.lineTo(0, unscaledHeight);
                borderShape.graphics.lineTo(0, 0);
                this.borderFill = new UIComponent();
                this.borderFill.mouseChildren = false;
                this.borderFill.mouseEnabled = false;
                this.borderFill.addChild(borderShape);
            }

            if (!this.borderFill.parent) {
                addChild(this.borderFill);
            }


        } else if (!highlighted) {
            if (this.borderFill && this.borderFill.parent) {
                removeChild(this.borderFill);
            }
            if (this.dragComp && this.dragComp.parent) {
                removeChild(this.dragComp);
            }
            if (!this.defaultBorderFill) {
                var defaultBorderShape:Shape = new Shape();
                defaultBorderShape.graphics.lineStyle(2, 0xB7BABC, 1);
                defaultBorderShape.graphics.moveTo(0, 0);
                defaultBorderShape.graphics.lineTo(unscaledWidth, 0);
                defaultBorderShape.graphics.lineTo(unscaledWidth, unscaledHeight);
                defaultBorderShape.graphics.lineTo(0, unscaledHeight);
                defaultBorderShape.graphics.lineTo(0, 0);
                this.defaultBorderFill = new UIComponent();
                this.defaultBorderFill.mouseChildren = false;
                this.defaultBorderFill.mouseEnabled = false;
                this.defaultBorderFill.addChild(defaultBorderShape);
            }

            if (!this.defaultBorderFill.parent) {
                addChild(this.defaultBorderFill);
            }
        }
    }

    private var highlighted:Boolean = false;

    public function highlight(analysisItem:AnalysisItem):void {
        if (dropArea.accept(analysisItem)) {
            highlighted = true;
            invalidateDisplayList();
        }
    }

    public function normal():void {
        highlighted = false;
        invalidateDisplayList();
    }
}
}
