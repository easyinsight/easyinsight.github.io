package com.easyinsight.analysis {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.AnalysisLongitude")]
public class AnalysisLongitude extends AnalysisCoordinate {
    public function AnalysisLongitude() {
        super();
    }


    override public function getType():int {
        return super.getType() | AnalysisItemTypes.LONGITUDE;
    }
}
}
