package com.easyinsight.analysis {
import flash.events.Event;
import flash.events.MouseEvent;

import mx.controls.Button;
import mx.controls.LinkButton;

[Event(name="modeChange", type="flash.events.Event")]
public class AdHocMode extends LinkButton {

    private var _adHoc:Boolean = true;

    public function AdHocMode() {
        super();
        addEventListener(MouseEvent.CLICK, onClick);
    }

    private function onClick(event:MouseEvent):void {
        adHoc = !adHoc;
        if (adHoc) {
            dispatchEvent(new Event("modeChange"));
        }
    }

    [Bindable(event="adHocChanged")]
    public function get adHoc():Boolean {
        return _adHoc;
    }

    public function set adHoc(value:Boolean):void {
        if (_adHoc == value) return;
        _adHoc = value;
        if (_adHoc) {
            toolTip = "Ad Hoc Report Mode";
        } else {
            toolTip = "Manual Report Mode";
        }
        dispatchEvent(new Event("adHocChanged"));
    }
}
}