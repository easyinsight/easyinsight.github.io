/**
 * Created by jamesboe on 9/19/14.
 */
package com.easyinsight {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.report.ReportExportButton;
import com.easyinsight.report.ReportView;

import flash.display.DisplayObject;

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
}
}
