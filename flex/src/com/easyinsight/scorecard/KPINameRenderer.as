package com.easyinsight.scorecard {
import com.easyinsight.kpi.KPI;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Alert;
import mx.controls.Label;
import mx.core.Application;
import mx.managers.PopUpManager;

public class KPINameRenderer extends HBox {

    private var kpi:KPI;
    private var kpiLabel:Label;

    private var _contextMenuAvailable:Boolean;

    public function KPINameRenderer() {
        super();
        kpiLabel = new Label();
        setStyle("verticalAlign", "middle");
        setStyle("fontSize", 14);
        setStyle("paddingLeft", 5);
        useHandCursor = true;
        buttonMode = true;
        mouseChildren = false;
        this.percentHeight = 100;
        //addEventListener(MouseEvent.CLICK, onClick);
    }


    [Bindable(event="contextMenuAvailableChanged")]
    public function get contextMenuAvailable():Boolean {
        return _contextMenuAvailable;
    }

    public function showCallout(text:String):void {
        
    }

    public function set contextMenuAvailable(value:Boolean):void {
        if (_contextMenuAvailable == value) return;
        _contextMenuAvailable = value;
        dispatchEvent(new Event("contextMenuAvailableChanged"));
        if (_contextMenuAvailable) {
            if (!hasEventListener(MouseEvent.CLICK)) {
                setStyle("textDecoration", "underline");
                addEventListener(MouseEvent.CLICK, onClick);
            }
        } else {
            if (hasEventListener(MouseEvent.CLICK)) {
                setStyle("textDecoration", "none");
                removeEventListener(MouseEvent.CLICK, onClick);
            }
        }
    }

    private function onClick(event:MouseEvent):void {

        var window:ScorecardContextWindow = new ScorecardContextWindow(kpi, passThrough, this);
        window.data = this.data;
        PopUpManager.addPopUp(window, this);
        var clippingY:uint = Application.application.height;

        window.x = event.stageX + 5;

        var startY:uint = event.stageY + 5;

        var endYPosition:uint;
        if (kpi.reports == null) {
            endYPosition = event.stageY + 25;
        } else {
            endYPosition = event.stageY + 25 + kpi.reports.length * 35;
        }        
        
        if (endYPosition > clippingY) {
            startY -= (endYPosition - clippingY); 
        }
        window.y = startY;
    }

    private function passThrough(event:Event):void {
        dispatchEvent(event);
    }

    protected override function createChildren():void {
        super.createChildren();
        addChild(kpiLabel);
    }

    override public function set data(val:Object):void {
        kpi = val as KPI;
        kpiLabel.text = kpi.name;
    }

    override public function get data():Object {
        return kpi;
    }
}
}