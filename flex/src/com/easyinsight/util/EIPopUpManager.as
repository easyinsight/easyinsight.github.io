package com.easyinsight.util {
import flash.display.Graphics;
import flash.display.Shape;

import mx.core.Application;
import mx.core.UIComponent;

public class EIPopUpManager {
    public function EIPopUpManager() {
    }

    public static function addPopUp(parent:UIComponent):void {
        var shape:Shape = new Shape();
        var g:Graphics = shape.graphics;
        shape.width = parent.width;
        shape.height = parent.height;
        g.clear();
        g.beginFill(0x000000, 0.5);
        g.drawRect(0, 0, parent.width, parent.height);
        g.endFill();
        parent.enabled = false;
        Application.application.addChild(shape);
        shape.x = parent.x;
        shape.y = parent.y;
    }
}
}