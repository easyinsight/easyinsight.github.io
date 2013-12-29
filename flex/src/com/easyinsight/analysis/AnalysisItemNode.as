package com.easyinsight.analysis {
[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.AnalysisItemNode")]
public class AnalysisItemNode extends FeedNode{
    public var analysisItem:AnalysisItem;
    public function AnalysisItemNode(analysisItem:AnalysisItem = null) {
        super();
        this.analysisItem = analysisItem;
    }

    override public function get display():String {
        if (analysisItem == null) {
            return "!!!! NULL !!!!";
        }
        return analysisItem.display;
    }

    override public function get unqualifiedDisplay():String {
        return analysisItem.unqualifiedDisplay;
    }
}
}