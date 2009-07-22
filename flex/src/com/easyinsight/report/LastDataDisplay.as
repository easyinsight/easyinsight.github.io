package com.easyinsight.report {
import flash.events.Event;

import mx.binding.utils.BindingUtils;
import mx.containers.HBox;
import mx.controls.Label;

public class LastDataDisplay extends HBox{

    private var _liveData:Boolean;
    private var _lastDataDate:Date;

    private var _labelText:String;

    private var _dateText:String;

    public function LastDataDisplay() {
        super();
    }


    [Bindable(event="labelTextChanged")]
    public function get labelText():String {
        return _labelText;
    }

    public function set labelText(value:String):void {
        if (_labelText == value) return;
        _labelText = value;
        dispatchEvent(new Event("labelTextChanged"));
    }

    [Bindable(event="dateTextChanged")]
    public function get dateText():String {
        return _dateText;
    }

    public function set dateText(value:String):void {
        if (_dateText == value) return;
        _dateText = value;
        dispatchEvent(new Event("dateTextChanged"));
    }

    public function set liveData(value:Boolean):void {
        if (_liveData == value) return;
        _liveData = value;
        dispatchEvent(new Event("liveDataChanged"));
    }

    public function set lastDataDate(value:Date):void {
        if (_lastDataDate == value) return;
        _lastDataDate = value;
        invalidateProperties();
        dispatchEvent(new Event("lastDataDateChanged"));
    }

    override protected function createChildren():void {
        super.createChildren();
        var leftLabel:Label = new Label();
        BindingUtils.bindProperty(leftLabel, "text", this, "labelText");
        var rightLabel:Label = new Label();
        BindingUtils.bindProperty(rightLabel, "text", this, "dateText");
        addChild(leftLabel);
        addChild(rightLabel);
    }

    override protected function commitProperties():void {
        super.commitProperties();
        if (_liveData) {
            labelText = "";
        } else {
            labelText = "";
        }
    }
}
}