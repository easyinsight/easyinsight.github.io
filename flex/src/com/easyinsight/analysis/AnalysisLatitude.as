package com.easyinsight.analysis {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.AnalysisLatitude")]
public class AnalysisLatitude extends AnalysisCoordinate {
    public function AnalysisLatitude() {
        super();
    }

    override public function getType():int {
        return super.getType() | AnalysisItemTypes.LATITUDE;
    }
}
}
