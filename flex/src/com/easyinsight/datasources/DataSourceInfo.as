package com.easyinsight.datasources {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.DataSourceInfo")]
public class DataSourceInfo {

    public static const STORED_PUSH:int = 1;
    public static const STORED_PULL:int = 2;
    public static const LIVE:int = 3;
    public static const COMPOSITE:int = 4;
    public static const COMPOSITE_PULL:int = 5;

    public var dataSourceName:String;
    public var dataSourceID:int;
    public var liveDataSource:Boolean;
    public var lastDataTime:Date;
    public var originName:String;

    public var type:int;

    public var childSources:ArrayCollection;

    public function DataSourceInfo() {
    }
}
}