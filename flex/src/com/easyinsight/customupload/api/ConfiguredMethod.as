package com.easyinsight.customupload.api {
import mx.collections.ArrayCollection;
[Bindable]
[RemoteClass(alias="com.easyinsight.api.dynamic.ConfiguredMethod")]
public class ConfiguredMethod {

    public static const UPDATE:int = 1;
    public static const DELETE:int = 2;

    public var methodType:int;
    public var methodName:String;
    public var keys:ArrayCollection;
}
}