package com.easyinsight.scorecard {
import com.easyinsight.kpi.KPI;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.containers.Canvas;
import mx.controls.Alert;
import mx.core.UIComponent;
import mx.events.FlexEvent;

public class KPIRenderer extends Canvas {

    protected var kpi:KPI;
    private var _contextMenuAvailable:Boolean;

    public function KPIRenderer() {
        super();
        mouseEnabled = true;
        //width = 100;
        //percentHeight = 100;
        mouseChildren = false;
        addEventListener(MouseEvent.CLICK, onClick);
        //setStyle("backgroundColor", 0xFF0000);
        /*useHandCursor = true;
        buttonMode = true;
        mouseChildren = false;*/
    }    

    private function onClick(event:MouseEvent):void {
        
    }

    [Bindable("dataChange")]
    override public function set data(val:Object):void {
        kpi = val as KPI;
        if (contextMenuAvailable) new ScorecardContextWindow(kpi, passThrough, this);        
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
        invalidateSize();
    }

    private function passThrough(event:Event):void {
        dispatchEvent(event);
    }

    [Bindable(event="contextMenuAvailableChanged")]
    public function get contextMenuAvailable():Boolean {
        return _contextMenuAvailable;
    }

    public function set contextMenuAvailable(value:Boolean):void {
        if (_contextMenuAvailable == value) return;
        _contextMenuAvailable = value;
        dispatchEvent(new Event("contextMenuAvailableChanged"));
        if (_contextMenuAvailable) {
            if (kpi != null) {
                new ScorecardContextWindow(kpi, passThrough, this);
            }
            setStyle("textDecoration", "underline");
        } else {
            setStyle("textDecoration", "none");
        }
    }

    override public function get data():Object {
        return kpi;
    }
}
}