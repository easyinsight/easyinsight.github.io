package com.easyinsight.report {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.skin.ImageDescriptor;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.ReportInfo")]
public class ReportInfo {

    public var admin:Boolean;
    public var report:AnalysisDefinition;
    public var accessDenied:Boolean;
    public var headerImage:ImageDescriptor;
    public var backgroundColor:uint;
    public var textColor:uint;
    public var configurations:ArrayCollection;

    public function ReportInfo() {
    }
}
}