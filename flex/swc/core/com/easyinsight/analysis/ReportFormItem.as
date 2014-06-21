package com.easyinsight.analysis {
import flash.events.Event;

import mx.containers.FormItem;
import mx.controls.CheckBox;

public class ReportFormItem extends FormItem {

    private var _property:String;
    private var _report:Object;
    private var _value:Object;
    private var _enabledProperty:String;
    private var _section:String;
    private var _explicitPosition:int = -1;
    public var reloadOnChange:Boolean;

    public function ReportFormItem(label:String = null, property:String = null, value:Object = null, report:Object = null,
                                   enabledProperty:String = null, reloadOnChange:Boolean = false, section:String = null,
            explicitPosition:int = -1) {
        super();
        this.label = label;
        this.value = value;
        this.report = report;
        this.property = property;
        this.enabledProperty = enabledProperty;
        this.section = section;
        this.explicitPosition = explicitPosition;
        this.reloadOnChange = reloadOnChange;
        direction = "horizontal";
    }

    public function get section():String {
        return _section;
    }

    public function set section(value:String):void {
        _section = value;
    }

    public function get explicitPosition():int {
        return _explicitPosition;
    }

    public function set explicitPosition(value:int):void {
        _explicitPosition = value;
    }

    public function get property():String {
        return _property;
    }

    public function set property(value:String):void {
        _property = value;
    }

    public function get report():Object {
        return _report;
    }

    public function set report(value:Object):void {
        _report = value;
    }

    [Bindable(event="valueChanged")]
    public function get value():Object {
        return _value;
    }

    public function set value(value:Object):void {
        if (_value == value) return;
        _value = value;
        dispatchEvent(new Event("valueChanged"));
    }

    public function get enabledProperty():String {
        return _enabledProperty;
    }

    public function set enabledProperty(value:String):void {
        _enabledProperty = value;
    }

    override protected function createChildren():void {
        super.createChildren();
        if (enabledProperty != null) {
            checkBox = new CheckBox();
            checkBox.selected = Boolean(report[enabledProperty]);
            addChild(checkBox);
        }
    }

    private var checkBox:CheckBox;

    public function validate():Boolean {
        return true;
    }

    protected function getValue():Object {
        return null;
    }

    public function save():void {
        report[property] = getValue();
        if (enabledProperty != null) {
            report[enabledProperty] = checkBox.selected;
        }
    }
}
}