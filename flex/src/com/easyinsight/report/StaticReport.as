package com.easyinsight.report {
import flash.utils.ByteArray;

[Bindable]
[RemoteClass(alias="com.easyinsight.solutions.StaticReport")]
public class StaticReport {

    public var reportID:int;
    public var reportImage:ByteArray;
    public var reportName:String;
    public var description:String;
    public var connectionID:int;
    public var tags:String;
    public var score:Number;
    public var author:String;
    public var creationDate:Date;

    public function StaticReport() {
    }
}
}