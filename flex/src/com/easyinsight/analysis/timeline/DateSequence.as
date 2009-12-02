package com.easyinsight.analysis.timeline {
[Bindable]
[RemoteClass(alias="com.easyinsight.sequence.DateSequence")]
public class DateSequence extends Sequence {

    public var dateType:int;

    public function DateSequence() {
        super();
    }

    override public function createEditor():ISequenceEditor {
        return new DateSequenceEditor();
    }
}
}