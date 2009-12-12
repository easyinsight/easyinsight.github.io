package com.easyinsight.reportpackage {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.reportpackage.ReportPackage")]
public class ReportPackage {

    public var name:String;
    public var reportPackageID:int;
    public var reports:ArrayCollection;
    public var marketplaceVisible:Boolean;
    public var connectionVisible:Boolean;
    public var publiclyVisible:Boolean;
    public var singleDataSource:Boolean;
    public var dataSourceID:int;
    public var description:String;
    public var authorName:String;
    public var dateCreated:Date;
    public var temporaryPackage:Boolean;

    public function ReportPackage() {
    }
}
}