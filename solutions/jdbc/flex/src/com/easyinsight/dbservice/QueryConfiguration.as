package com.easyinsight.dbservice {
[Bindable]
[RemoteClass(alias="com.easyinsight.dbservice.QueryConfiguration")]
public class QueryConfiguration {
    public var query:String;
    public var dataSource:String;
    public var adHoc:Boolean;
    public var queryConfigurationID:int;
    public var name:String;
    public var publishMode:int;
    
    public function QueryConfiguration() {
    }
}
}