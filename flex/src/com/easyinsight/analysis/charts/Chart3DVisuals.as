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
        _rotationAngle = val;
        _chart["rotationAngle"] = val;
    }

    [Bindable]
    public function get elevationAngle():Number {
        return _elevationAngle;
    }

    public function set elevationAngle(val:Number):void {
        _elevationAngle = val;
        _chart["elevationAngle"] = val;
    }

    private function downListener(e:MouseEvent):void {
        _chart.removeEventListener(MouseEvent.MOUSE_DOWN, downListener);
        Application.application.addEventListener(MouseEvent.MOUSE_UP, upListener);
        Application.application.addEventListener(MouseEvent.ROLL_OUT, rollOutListener);

        _chart.addEventListener(MouseEvent.MOUSE_MOVE, moveListener);
        downX = e.stageX;
        downY = e.stageY;
        downElevation = elevationAngle;
        downRotation = rotationAngle;
    }

    private function rollOverListener(e:MouseEvent):void {
        Application.application.removeEventListener(MouseEvent.ROLL_OVER, rollOverListener);
        if (! e.buttonDown)
            upListener(e);
    }

    private function rollOutListener(e:MouseEvent):void {
        Application.application.removeEventListener(MouseEvent.ROLL_OUT, rollOutListener);
        Application.application.addEventListener(MouseEvent.ROLL_OVER, rollOverListener);
    }

    private function moveListener(e:MouseEvent):void {
        var dx:Number = e.stageX - downX;
        var dy:Number = e.stageY - downY;
        elevationAngle = downElevation + dy / 5;
        rotationAngle = downRotation - dx / 5;
    }

    private function upListener(e:MouseEvent):void {
        dispatchEvent(new ChartRotationEvent(elevationAngle, rotationAngle));
        _chart.addEventListener(MouseEvent.MOUSE_DOWN, downListener);
        Application.application.removeEventListener(MouseEvent.MOUSE_UP, upListener);
        _chart.removeEventListener(MouseEvent.MOUSE_MOVE, moveListener);
    }
}
}