package com.easyinsight.dbservice {
[Bindable]
[RemoteClass(alias="com.easyinsight.dbservice.GenericDBConfiguration")]
public class GenericDBConfiguration extends DatabaseConfiguration{

    public var driver:String;
    public var jdbcURL:String;

    public function GenericDBConfiguration() {
        super();
    }
}
}