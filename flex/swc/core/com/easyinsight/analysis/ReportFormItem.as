package com.easyinsight.analysis {
import mx.containers.FormItem;

public class ReportFormItem extends FormItem {

    public var property:String;
    public var report:Object;
    public var value:Object;

    public function ReportFormItem(label:String, property:String, value:Object, report:Object) {
        super();
        this.label = label;
        this.value = value;
        this.report = report;
        this.property = property;
    }

    protected function getValue():Object {
        return null;
    }

    public function save():void {
        report[property] = getValue();
    }
}
}