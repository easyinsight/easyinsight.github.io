package com.easyinsight.analysis {
import flash.events.Event;
import flash.events.MouseEvent;

import mx.controls.Button;

[Event(name="modeChange", type="flash.events.Event")]
public class AdHocMode extends Button {

    private var _adHoc:Boolean = true;

    [Embed(source="../../../../assets/nav_refresh_green.png")]
    private var adHocMode:Class;

    [Embed(source="../../../../assets/nav_refresh_red.png")]
    private var manualMode:Class;

    public function AdHocMode() {
        super();
        setStyle("icon", adHocMode);
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
            setStyle("icon", adHocMode);
            toolTip = "Ad Hoc Report Mode";
        } else {
            setStyle("icon", manualMode);
            toolTip = "Manual Report Mode";
        }
        dispatchEvent(new Event("adHocChanged"));
    }
}
}