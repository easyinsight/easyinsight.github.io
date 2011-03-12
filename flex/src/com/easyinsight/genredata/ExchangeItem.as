package com.easyinsight.genredata {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.exchange.ExchangeItem")]
public class ExchangeItem {

    public var name:String;
    public var id:int;
    public var attribution:String;
    public var installs:int;
    public var dateAdded:Date;
    public var tags:ArrayCollection;
    public var description:String;
    public var author:String;

    public function ExchangeItem() {
    }        
}
}