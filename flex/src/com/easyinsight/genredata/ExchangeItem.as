package com.easyinsight.genredata {
import com.easyinsight.quicksearch.EIDescriptor;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.exchange.ExchangeItem")]
public class ExchangeItem {

    public var name:String;
    public var id:int;
    public var installs:int;
    public var dateAdded:Date;
    public var description:String;
    public var author:String;
    public var descriptor:EIDescriptor;
    public var solutionID:int;
    public var solutionName:String;
    public var url:String;

    public function ExchangeItem() {
    }        
}
}