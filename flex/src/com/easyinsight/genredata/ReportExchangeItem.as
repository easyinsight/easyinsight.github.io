package com.easyinsight.genredata {

[Bindable]
[RemoteClass(alias="com.easyinsight.exchange.ReportExchangeItem")]
public class ReportExchangeItem extends ExchangeItem{

    public var reportType:int;
    public var dataSourceID:int;
    public var dataSourceName:String;
    public var dataSourceAccessible:Boolean;

    public function ReportExchangeItem() {
        super();
    }
}
}