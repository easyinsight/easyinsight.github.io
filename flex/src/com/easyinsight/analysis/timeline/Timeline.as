package com.easyinsight.analysis.timeline {
import com.easyinsight.analysis.AnalysisDefinition;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSTimeline")]
public class Timeline extends AnalysisDefinition {
    
    public var sequence:Sequence;
    public var report:AnalysisDefinition;
    public var timelineID:int;

    public function Timeline() {
        super();
    }

    override public function get type():int {
        return AnalysisDefinition.TIMELINE;
    }
}
}