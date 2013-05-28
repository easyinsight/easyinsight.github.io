/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 5/16/11
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.listing {
import com.easyinsight.framework.PerspectiveInfo;
import com.easyinsight.genredata.AnalyzeEvent;
import com.easyinsight.report.ReportAnalyzeSource;
import com.easyinsight.util.ActionDashboardLog;
import com.easyinsight.util.ActionDataSourceLog;
import com.easyinsight.util.ActionLog;
import com.easyinsight.util.ActionReportLog;
import com.easyinsight.util.MultiLineButton;
import com.easyinsight.util.MultiLineLinkButton;

import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Text;

public class LastActionLink extends Text {

    private var actionLog:ActionLog;

    public function LastActionLink() {
        styleName = "fallThroughFonts";
        setStyle("color", 0x222222);
        addEventListener(MouseEvent.CLICK, onClick);
        addEventListener(MouseEvent.ROLL_OVER, onRollover);
        addEventListener(MouseEvent.ROLL_OUT, onRollout);
        setStyle("textAlign", "center");
        this.width = 180;
        this.maxWidth = 180;
        useHandCursor = true;
        buttonMode = true;
        mouseEnabled = true;
        mouseChildren = false;
        selectable = false;
    }

    private function onRollover(event:MouseEvent):void {
        setStyle("textDecoration", "underline");
    }

    private function onRollout(event:MouseEvent):void {
        setStyle("textDecoration", "none");
    }

    private function onClick(event:MouseEvent):void {
        if (actionLog is ActionDataSourceLog) {
            dispatchEvent(new AnalyzeEvent(new DescriptorAnalyzeSource(ActionDataSourceLog(actionLog).dataSourceID)));
        } else if (actionLog is ActionReportLog) {
            if (actionLog.actionType == ActionReportLog.EDIT) {
                dispatchEvent(new AnalyzeEvent(new AnalysisDefinitionAnalyzeSource(ActionReportLog(actionLog).insightDescriptor)));
            } else if (actionLog.actionType == ActionReportLog.VIEW) {
                dispatchEvent(new AnalyzeEvent(new ReportAnalyzeSource(ActionReportLog(actionLog).insightDescriptor)));
            }
        } else if (actionLog is ActionDashboardLog) {
            if (actionLog.actionType == ActionDashboardLog.EDIT) {
                dispatchEvent(new AnalyzeEvent(new PerspectiveInfo(PerspectiveInfo.DASHBOARD_EDITOR, {dashboardID: ActionDashboardLog(actionLog).dashboardDescriptor.id })));
            } else if (actionLog.actionType == ActionDashboardLog.VIEW) {
                dispatchEvent(new AnalyzeEvent(new PerspectiveInfo(PerspectiveInfo.DASHBOARD_VIEW, {dashboardID: ActionDashboardLog(actionLog).dashboardDescriptor.id })));
            }
        }
    }

    override public function set data(val:Object):void {
        actionLog = val as ActionLog;
        if (actionLog != null) {
            this.text = actionLog.display;
        }
    }

    override public function get data():Object {
        return actionLog;
    }
}
}
