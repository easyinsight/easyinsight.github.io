package com.easyinsight.analysis {


import com.easyinsight.util.SimpleErrorView;

import mx.core.UIComponent;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.GenericReportFault")]
public class GenericReportFault extends ReportFault {

    public var message:String;

    public function GenericReportFault() {
        super();
    }

    override public function createFaultWindow():UIComponent {
        var window:SimpleErrorView = new SimpleErrorView();
        window.errorMessage = message;
        return window;
    }
}
}