/**
 * Created by jamesboe on 9/19/14.
 */
package com.easyinsight {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.report.ReportExportButton;
import com.easyinsight.report.ReportURLWindow;

import flash.display.DisplayObject;

import mx.managers.PopUpManager;

public class ReportEditorExportButton extends ReportExportButton {
    public function ReportEditorExportButton() {
    }

    override protected function getReport():AnalysisDefinition {
        return getReportEditor(this).analysisDefinition;
    }

    private function getReportEditor(comp:DisplayObject):SimpleReportEditor {
        if (comp is SimpleReportEditor) {
            return comp as SimpleReportEditor;
        }
        return getReportEditor(comp.parent);
    }

    override protected function getCoreView():DisplayObject {
        return getReportEditor(this).dataView.getCoreView();
    }

    override protected function url():void {
        var urlWindow:ReportURLWindow = new ReportURLWindow();
        urlWindow.report = getReport();
        urlWindow.x = this.x;
        urlWindow.y = this.y;
        PopUpManager.addPopUp(urlWindow, this, true);
        PopUpManager.removePopUp(this);
    }
}
}
