/**
 * Created by jamesboe on 9/19/14.
 */
package com.easyinsight {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.dashboard.DashboardStackPositions;
import com.easyinsight.report.ReportExportButton;
import com.easyinsight.report.ReportURLWindow;
import com.easyinsight.report.ReportView;

import flash.display.DisplayObject;

import mx.managers.PopUpManager;

public class ReportEndUserExportButton extends ReportExportButton {
    public function ReportEndUserExportButton() {
    }

    override protected function getReport():AnalysisDefinition {
        return getReportView(this).viewFactory.report;
    }

    private function getReportView(comp:DisplayObject):ReportView {
        if (comp is ReportView) {
            return comp as ReportView;
        }
        return getReportView(comp.parent);
    }

    override protected function getCoreView():DisplayObject {
        return getReportView(this).viewFactory.getChildAt(0);
    }


    override protected function url():void {
        var pos:DashboardStackPositions = new DashboardStackPositions();
        getReportView(this).positionsPopulate(pos);
        var urlWindow:ReportURLWindow = new ReportURLWindow();
        urlWindow.report = getReport();
        urlWindow.pos = pos;
        urlWindow.x = this.x;
        urlWindow.y = this.y;
        PopUpManager.addPopUp(urlWindow, this, true);
        PopUpManager.removePopUp(this);
    }
}
}
