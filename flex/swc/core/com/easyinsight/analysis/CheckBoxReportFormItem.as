package com.easyinsight.analysis {
import flash.events.Event;

import mx.controls.CheckBox;


public class CheckBoxReportFormItem extends ReportFormItem {

    private var checkBox:CheckBox;
    
    private var modifierFunction:Function;

    public function CheckBoxReportFormItem(label:String, property:String, value:Object, report:Object, enabledProperty:String = null, reloadOnChange:Boolean = false,
            modifierFunction:Function = null) {
        super(label, property, value, report, enabledProperty, reloadOnChange);
        this.modifierFunction = modifierFunction;
    }

    protected override function createChildren():void {
        super.createChildren();
        checkBox = new CheckBox();
        if (this.value != null) {
            checkBox.selected = Boolean(value);
        }
        if (this.reloadOnChange) {
            checkBox.addEventListener(Event.CHANGE, function(event:Event):void {
                dispatchEvent(new Event('refreshNecessary', true));
            });
        }
        addChild(checkBox);
        if (modifierFunction) {
            this.enabled = modifierFunction.call(this, report);
        }
    }

    override protected function getValue():Object {
        return checkBox.selected;
    }
}
}