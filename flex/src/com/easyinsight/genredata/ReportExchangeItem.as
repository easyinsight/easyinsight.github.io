package com.easyinsight.genredata {
import flash.utils.ByteArray;

[Bindable]
[RemoteClass(alias="com.easyinsight.exchange.ReportExchangeItem")]
public class ReportExchangeItem extends ExchangeItem{

    public var reportType:int;
    public var dataSourceID:int;
    public var dataSourceName:String;
    public var dataSourceAccessible:Boolean;
    public var image:ByteArray;

    public function ReportExchangeItem() {
        super();
    }
}
}