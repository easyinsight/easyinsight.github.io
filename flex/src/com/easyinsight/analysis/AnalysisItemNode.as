package com.easyinsight.analysis {
[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.AnalysisItemNode")]
public class AnalysisItemNode extends FeedNode{
    public var analysisItem:AnalysisItem;
    public function AnalysisItemNode() {
        super();
    }

    override public function get display():String {
        return analysisItem.display;
    }
}
}