package com.easyinsight.genredata {
import com.easyinsight.quicksearch.EIDescriptor;

[Bindable]
[RemoteClass(alias="com.easyinsight.exchange.ReportExchangeItem")]
public class ReportExchangeItem extends ExchangeItem{

    public var descriptor:EIDescriptor;

    public function ReportExchangeItem() {
        super();
    }
}
}