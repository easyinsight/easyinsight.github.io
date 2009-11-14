package com.easyinsight.listing {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.userupload.MyDataTree")]
public class MyDataTree {

    public var objects:ArrayCollection;
    public var includeGroup:Boolean;

    public function MyDataTree() {
    }
}
}