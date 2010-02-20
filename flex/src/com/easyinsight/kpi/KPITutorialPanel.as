package com.easyinsight.kpi {
import mx.containers.VBox;

public class KPITutorialPanel extends VBox {

    private var _kpiData:KPIData;

    public function KPITutorialPanel() {
        super();
    }

    public function get kpiData():KPIData {
        return _kpiData;
    }

    public function set kpiData(value:KPIData):void {
        _kpiData = value;
    }
}
}