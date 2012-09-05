package com.easyinsight.analysis {

import mx.collections.ArrayCollection;

import mx.core.UIComponent;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.StorageLimitFault")]
public class StorageLimitFault extends ReportFault {

    public var message:String;
    public var statsList:ArrayCollection;

    public function StorageLimitFault() {
        super();
    }
    
    override public function createFaultWindow():UIComponent {
        var window:StorageLimitWindow = new StorageLimitWindow();
        window.message = message;
        window.statsList = statsList;
        return window;
    }

    override public function getMessage():String {
        return message;
    }
}
}