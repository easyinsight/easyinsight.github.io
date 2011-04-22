package com.easyinsight.analysis.charts {
import flash.display.DisplayObject;
import flash.events.EventDispatcher;
import flash.events.MouseEvent;

import mx.core.Application;

public class Chart3DVisuals extends EventDispatcher {

    private var _rotationAngle:Number = 43.50;
    private var _elevationAngle:Number = 31.50;

    private var downX:int;
    private var downY:int;
    private var downElevation:Number;
    private var downRotation:Number;

    private var _chart:DisplayObject;

    public function Chart3DVisuals() {
    }

    public function set chart(val:DisplayObject):void {
        _chart = val;
        _chart.addEventListener(MouseEvent.MOUSE_DOWN, downListener);
    }

    [Bindable]
    public function get rotationAngle():Number {
        return _rotationAngle;
    }

    public function set rotationAngle(val:Number):void {
        if (val < 3) {
            val = 3;
        }
        if (val > 86) {
            val = 86;
        }
        _rotationAngle = val;
        _chart["rotationAngle"] = val;
    }

    [Bindable]
    public function get elevationAngle():Number {
        return _elevationAngle;
    }

    public function set elevationAngle(val:Number):void {
        if (val < 3) {
            val = 3;
        }
        if (val > 86) {
            val = 86;
        }
        _elevationAngle = val;
        _chart["elevationAngle"] = val;
    }

    private function downListener(e:MouseEvent):void {
        try {
            _chart.removeEventListener(MouseEvent.MOUSE_DOWN, downListener);
            Application.application.addEventListener(MouseEvent.MOUSE_UP, upListener);
            Application.application.addEventListener(MouseEvent.ROLL_OUT, rollOutListener);

            _chart.addEventListener(MouseEvent.MOUSE_MOVE, moveListener);
            downX = e.stageX;
            downY = e.stageY;
            downElevation = elevationAngle;
            downRotation = rotationAngle;
        } catch(e:Error) {
        }
    }

    private function rollOverListener(e:MouseEvent):void {
        try {
            Application.application.removeEventListener(MouseEvent.ROLL_OVER, rollOverListener);
            if (! e.buttonDown)
                upListener(e);
        } catch(e:Error) {
        }
    }

    private function rollOutListener(e:MouseEvent):void {
        try {
            Application.application.removeEventListener(MouseEvent.ROLL_OUT, rollOutListener);
            Application.application.addEventListener(MouseEvent.ROLL_OVER, rollOverListener);
        } catch(e:Error) {
        }
    }

    private function moveListener(e:MouseEvent):void {
        try {
            var dx:Number = e.stageX - downX;
            var dy:Number = e.stageY - downY;
            elevationAngle = downElevation + dy / 5;
            rotationAngle = downRotation - dx / 5;
        } catch(e:Error) {
        }
    }

    private function upListener(e:MouseEvent):void {
        try {
            dispatchEvent(new ChartRotationEvent(elevationAngle, rotationAngle));
            _chart.addEventListener(MouseEvent.MOUSE_DOWN, downListener);
            Application.application.removeEventListener(MouseEvent.MOUSE_UP, upListener);
            _chart.removeEventListener(MouseEvent.MOUSE_MOVE, moveListener);
        } catch(e:Error) {
        }
    }
}
}