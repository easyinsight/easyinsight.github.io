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

    public function get dataSourceName():String {
        if (exchangeData is ExchangeReportData) {
            var exchangeReportData:ExchangeReportData = exchangeData as ExchangeReportData;
            return exchangeReportData.dataSourceName;
        }
        return null;
    }
}
}