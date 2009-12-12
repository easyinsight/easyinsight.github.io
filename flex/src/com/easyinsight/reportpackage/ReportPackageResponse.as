package com.easyinsight.reportpackage {
[Bindable]
[RemoteClass(alias="com.easyinsight.reportpackage.ReportPackageResponse")]
public class ReportPackageResponse {

    public static const SUCCESS:int = 1;
    public static const NEED_LOGIN:int = 2;
    public static const REJECTED:int = 3;

    public var status:int;
    public var reportPackageDescriptor:ReportPackageDescriptor;

    public function ReportPackageResponse() {
    }
}
}