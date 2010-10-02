package com.easyinsight.util {
import flash.display.DisplayObject;
import flash.geom.Point;

import mx.core.Application;
import mx.core.IFlexDisplayObject;

public class PopUpUtil {
    public function PopUpUtil() {
    }

    public static function centerPopUp(popUp:IFlexDisplayObject):void {
        var appWidth:int = Application.application.width;
        var appHeight:int = Application.application.height;

        // The appWidth may smaller than parentWidth if the application is
        // clipped by the parent application.
        var x:int = Math.max(0, appWidth - popUp.width) / 2;
        var y:int = Math.max(0, appHeight - popUp.height) / 2;

        var pt:Point = new Point(0, 0);
        //pt = popUpParent.localToGlobal(pt);
        //pt = popUp.parent.globalToLocal(pt);
        popUp.move(Math.round(x) + pt.x, Math.round(y) + pt.y);
    }

    public static function centerPopUpWithY(popUp:IFlexDisplayObject, fixedY:int):void {
        var appWidth:int = Application.application.width;
        var appHeight:int = Application.application.height;

        // The appWidth may smaller than parentWidth if the application is
        // clipped by the parent application.
        var x:int = Math.max(0, appWidth - popUp.width) / 2;
        
        var pt:Point = new Point(0, 0);
        //pt = popUpParent.localToGlobal(pt);
        //pt = popUp.parent.globalToLocal(pt);
        popUp.move(Math.round(x) + pt.x, Math.round(fixedY) + pt.y);
    }
}
}