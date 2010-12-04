package com.easyinsight.dashboard {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.dashboard.Dashboard")]
public class Dashboard {

    public var name:String;
    public var id:int;
    public var urlKey:String;
    public var accountVisible:Boolean;
    public var rootElement:DashboardElement;
    public var administrators:ArrayCollection;
    public var dataSourceID:int;
    public var filters:ArrayCollection;
    public var exchangeVisible:Boolean;
    public var description:String;
    public var authorName:String;
    public var creationDate:Date;
    public var updateDate:Date;

    public function Dashboard() {
    }
}
}