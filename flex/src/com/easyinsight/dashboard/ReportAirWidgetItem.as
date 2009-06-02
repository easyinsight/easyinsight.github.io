package com.easyinsight.dashboard {
import com.easyinsight.solutions.InsightDescriptor;

import mx.core.Container;

public class ReportAirWidgetItem extends AirWidgetItem{

    public var report:InsightDescriptor;

    public function ReportAirWidgetItem(report:InsightDescriptor) {
        super();
        this.name = report.name;
        this.report = report;
    }

    override public function createDisplayObject():Container {
        var dataViewPanel:AirViewPanel = new AirViewPanel();
        dataViewPanel.analysisID = report.id;
        dataViewPanel.reportType = report.reportType;
        return dataViewPanel;
    }

    override public function refreshData():void {
        var panel:AirViewPanel = displayObject as AirViewPanel;
        panel.refreshData();
    }
}
}