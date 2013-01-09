/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 1/7/13
 * Time: 1:23 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.dashboard {
import flash.events.MouseEvent;

import mx.containers.Canvas;
import mx.controls.Alert;
import mx.controls.Label;
import mx.core.UIComponent;
import mx.events.ItemClickEvent;

public class DashboardButton extends Canvas {

    private var labelObj:Label;

    public function DashboardButton() {
        labelObj = new Label();
        labelObj.setStyle("color", 0xFFFFFF);
        addEventListener(MouseEvent.CLICK, onClick);
        mouseEnabled = true;
        mouseChildren = false;
        buttonMode = true;
        this.height = 22;
    }

    private var _data:Object;

    override public function set data(val:Object):void {
        _data = val;
    }

    override public function get data():Object {
        return _data;
    }

    private function onClick(event:MouseEvent):void {
        dispatchEvent(new ItemClickEvent(ItemClickEvent.ITEM_CLICK));
    }

    public function set text(val:String):void {
        labelObj.text = val;
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(labelObj);
    }

    private var hovered:Boolean;
    private var _selected:Boolean;

    public function get selected():Boolean {
        return _selected;
    }

    private var invalidateSelection:Boolean;

    public function set selected(value:Boolean):void {
        if (value != _selected) {
            invalidateSelection = true;
        }
        _selected = value;
        if (invalidateSelection) {
            invalidateDisplayList();
        }
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);

        if (invalidateSelection) {
            if (selected) {
                labelObj.setStyle("color", 0x0);
                graphics.beginFill(0xFFFFFF);
                graphics.drawRoundRectComplex(0, 0, unscaledWidth, unscaledHeight, 5, 5, 5, 5);
                graphics.endFill();
            } else {
                labelObj.setStyle("color", 0xFFFFFF);
                graphics.clear();
            }
        }
        invalidateSelection = false;
    }
}
}
