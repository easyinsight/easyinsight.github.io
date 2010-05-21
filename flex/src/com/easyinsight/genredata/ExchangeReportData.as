package com.easyinsight.genredata {
[Bindable]
[RemoteClass(alias="com.easyinsight.exchange.ExchangeReportData")]
public class ExchangeReportData extends ExchangeData {

    public var reportType:int;
    public var dataSourceID:int;
    public var dataSourceName:String;
    public var dataSourceAccessible:Boolean;
    public var reportUrlKey:String;

    public function ExchangeReportData() {
        super();
    }
}
}