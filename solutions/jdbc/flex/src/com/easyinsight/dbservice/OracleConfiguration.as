package com.easyinsight.dbservice {
[Bindable]
[RemoteClass(alias="com.easyinsight.dbservice.SQLServerConfiguration")]
public class OracleConfiguration extends DatabaseConfiguration{

    public var host:String;
    public var port:String;
    public var schemaName:String;
    public var userName:String;
    public var password:String;

    public function OracleConfiguration() {
        super();
    }
}
}