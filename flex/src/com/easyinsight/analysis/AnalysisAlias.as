package com.easyinsight.analysis {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.AnalysisAlias")]
public class AnalysisAlias extends AnalysisItem {

    public var analysisItem:AnalysisItem;

    public function AnalysisAlias() {
        super();
    }

    override public function getType():int {
        return analysisItem.getType();
    }
}
}