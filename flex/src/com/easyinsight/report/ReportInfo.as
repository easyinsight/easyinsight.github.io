package com.easyinsight.report {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.ReportInfo")]
public class ReportInfo {

    public var role:int;
    public var name:String;
    public var tags:String;
    public var attribution:String;
    public var ownerName:String;
    public var viewCount:int;
    public var rating:Number;
    public var ratingCount:int;

    public function ReportInfo() {
    }
}
}