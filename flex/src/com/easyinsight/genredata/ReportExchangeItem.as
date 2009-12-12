package com.easyinsight.genredata {
import flash.utils.ByteArray;

[Bindable]
[RemoteClass(alias="com.easyinsight.exchange.ReportExchangeItem")]
public class ReportExchangeItem extends ExchangeItem{

    public var exchangeData:ExchangeData;
    public var image:ByteArray;

    public function ReportExchangeItem() {
        super();
    }
}
}