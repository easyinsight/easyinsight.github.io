package com.easyinsight.analysis {
import com.easyinsight.util.ErrorReportView;

import mx.core.UIComponent;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.ServerError")]
public class ServerError extends ReportFault {

    public var message:String;

    public function ServerError() {
        super();
    }

    override public function createFaultWindow():UIComponent {
        var window:ErrorReportView = new ErrorReportView();
        window.errorMessage = message;
        return window;
    }

    override public function getMessage():String {
        return message;
    }
}
}