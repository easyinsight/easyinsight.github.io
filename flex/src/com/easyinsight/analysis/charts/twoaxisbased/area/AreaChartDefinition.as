package com.easyinsight.analysis.charts.twoaxisbased.area {
import com.easyinsight.analysis.charts.twoaxisbased.TwoAxisDefinition;
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSAreaChartDefinition")]
public class AreaChartDefinition extends TwoAxisDefinition{
    public function AreaChartDefinition() {
        super();
    }

    override public function getLabel():String {
        return "2D Area";
    }

    override public function get controller():String {
        return "com.easyinsight.analysis.charts.twoaxisbased.area.AreaChartController";
    }
}
}