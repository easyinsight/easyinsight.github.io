/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/14/11
 * Time: 9:10 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.verticallist {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.CustomChangeEvent;
import com.easyinsight.analysis.DataServiceEvent;
import com.easyinsight.analysis.IReportControlBar;
import com.easyinsight.analysis.ReportControlBar;
import com.easyinsight.util.PopUpUtil;

import flash.events.MouseEvent;

import mx.controls.Button;
import mx.managers.PopUpManager;

public class CombinedVerticalListControlBar extends ReportControlBar implements IReportControlBar {

    private var listDefinition:CombinedVerticalListDefinition;

    public function CombinedVerticalListControlBar() {
        var button:Button = new Button();
        button.label = "Configure...";
        button.addEventListener(MouseEvent.CLICK, configureReport);
        addChild(button);
    }

    private function configureReport(event:MouseEvent):void {
        var window:CombinedVerticalListWindow = new CombinedVerticalListWindow();
        window.report = listDefinition;
        PopUpManager.addPopUp(window, this, true);
        PopUpUtil.centerPopUp(window);
    }

    public function set analysisDefinition(analysisDefinition:AnalysisDefinition):void {
        listDefinition = analysisDefinition as CombinedVerticalListDefinition;
    }

    public function createAnalysisDefinition():AnalysisDefinition {
        return listDefinition;
    }

    public function isDataValid():Boolean {
        return listDefinition.reports.length > 0;
    }

    public function addItem(analysisItem:AnalysisItem):void {
    }

    public function onCustomChangeEvent(event:CustomChangeEvent):void {
    }

    public function onDataReceipt(event:DataServiceEvent):void {
    }
}
}
