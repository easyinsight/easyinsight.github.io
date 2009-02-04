package com.easyinsight.customupload.api {

[Bindable]
[RemoteClass(alias="com.easyinsight.api.DataSourceAPIDescriptor")]
public class DataSourceAPIDescriptor {

    public var feedID:int;
    public var name:String;
    public var apiKey:String;
    public var uncheckedEnabled:Boolean;
    public var validatedEnabled:Boolean;
    public var dynamicServiceDescriptor:DynamicServiceDescriptor;

    public function DataSourceAPIDescriptor() {
    }
}
}