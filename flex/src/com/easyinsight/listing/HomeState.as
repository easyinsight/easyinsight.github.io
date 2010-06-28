package com.easyinsight.listing {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.HomeState")]
public class HomeState {

    public var dataSources:ArrayCollection;
    public var reports:ArrayCollection;

    public function HomeState() {
    }
}
}