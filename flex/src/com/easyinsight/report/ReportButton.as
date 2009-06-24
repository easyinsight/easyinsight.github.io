package com.easyinsight.report {

import com.easyinsight.quicksearch.EIDescriptor;

import mx.controls.Button;

public class ReportButton extends Button {

    private var report:EIDescriptor;

    private var _reportSelected:Boolean;
    
    public function ReportButton() {
        super();
        setStyle("fontSize", 14);
    }

    override public function set data(val:Object):void {
        this.report = val as EIDescriptor;
        this.label = this.report.name;
    }

    override public function get data():Object {
        return report;
    }
}
}