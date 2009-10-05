package com.easyinsight.genredata {
[Bindable]
[RemoteClass(alias="com.easyinsight.exchange.SolutionReportExchangeItem")]
public class SolutionReportExchangeItem extends ReportExchangeItem{

    public var solutionID:int;
    public var solutionName:String;

    public function SolutionReportExchangeItem() {
        super();
    }
}
}