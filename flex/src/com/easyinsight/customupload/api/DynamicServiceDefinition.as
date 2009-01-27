package com.easyinsight.customupload.api {
import mx.collections.ArrayCollection;
[Bindable]
[RemoteClass(alias="com.easyinsight.api.dynamic.DynamicServiceDefinition")]
public class DynamicServiceDefinition {
    public var feedID:int;
    public var serviceID:int;
    public var configuredMethods:ArrayCollection;
}
}