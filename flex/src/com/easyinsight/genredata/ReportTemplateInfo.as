package com.easyinsight.genredata {
import com.easyinsight.quicksearch.EIDescriptor;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.solutions.ReportTemplateInfo")]
public class ReportTemplateInfo {

    public var exchangeData:SolutionReportExchangeItem;
    public var dataSources:ArrayCollection;

    public function ReportTemplateInfo() {
    }
}
}