package com.easyinsight.analysis {
import flash.display.Bitmap;
import flash.display.BitmapData;
import flash.events.MouseEvent;

import mx.controls.Image;
import mx.controls.Label;
import mx.core.DragSource;
import mx.managers.DragManager;

public class AnalysisItemLabel extends Label{

    public var wrapper:AnalysisItemWrapper;
    private var dragEnabled:Boolean = false;

    public function AnalysisItemLabel() {
        super();
    }

    override public function set data(val:Object):void {
        this.wrapper = val as AnalysisItemWrapper;
        text = this.wrapper.displayName;
        if (wrapper.feedNode is AnalysisItemNode) {
            if (!dragEnabled) {
                dragEnabled = true;
                addEventListener(MouseEvent.MOUSE_DOWN, startDragging);
            }
        } else {
            if (dragEnabled) {
                dragEnabled = false;
                removeEventListener(MouseEvent.MOUSE_DOWN, startDragging);
            }
        }
    }

    private function startDragging(event:MouseEvent):void {
        var dragSource:DragSource = new DragSource();
        var bd:BitmapData = new BitmapData(this.width, this.height);
        bd.draw(this);
        var bitmap:Bitmap = new Bitmap(bd);
        var image:Image = new Image();
        image.source = bitmap;
        DragManager.doDrag(this, dragSource, event, image);
    }

    override public function get data():Object {
        return this.wrapper;
    }
}
}