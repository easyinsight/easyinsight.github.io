package com.easyinsight.genredata {

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.solutions.ReportTemplateInfo")]
public class ReportTemplateInfo {

    public var exchangeData:ExchangeItem;
    public var dataSources:ArrayCollection;

    public function ReportTemplateInfo() {
    }
}
}