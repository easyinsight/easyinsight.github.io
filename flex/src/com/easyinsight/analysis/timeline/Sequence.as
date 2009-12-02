package com.easyinsight.analysis.timeline {
import com.easyinsight.analysis.AnalysisItem;
[Bindable]
[RemoteClass(alias="com.easyinsight.sequence.Sequence")]
public class Sequence {

    public var analysisItem:AnalysisItem;
    public var sequenceID:int;

    public function Sequence() {
    }

    public function createEditor():ISequenceEditor {
        return null;
    }
}
}