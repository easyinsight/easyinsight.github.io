package com.easyinsight.analysis {
import mx.core.UIComponent;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.AnalysisItemFault")]
public class AnalysisItemFault extends ReportFault {

    public var message:String;
    public var analysisItem:AnalysisItem;

    public function AnalysisItemFault() {
        super();
    }

    override public function createFaultWindow():UIComponent {
        var window:SimpleErrorDisplay = new SimpleErrorDisplay();
        window.message = message;
        return window;
    }
}
}