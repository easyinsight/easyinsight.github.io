package com.easyinsight.listing {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.userupload.MyDataTree")]
public class MyDataTree {

    public var objects:ArrayCollection;
    public var includeGroup:Boolean;
    public var reportCount:int;
    public var dashboardCount:int;
    public var dataSourceCount:int;

    public function MyDataTree() {
    }
}
}