/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/5/11
 * Time: 11:59 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.baseviews {
import com.easyinsight.reportviews.DashboardView;
import com.easyinsight.reportviews.ReportMetadata;
import com.easyinsight.reportviews.ReportView;
import com.easyinsight.reportviews.ScorecardView;
import com.easyinsight.util.ActionDashboardLog;
import com.easyinsight.util.ActionLog;
import com.easyinsight.util.ActionReportLog;
import com.easyinsight.util.ActionScorecardLog;

import flash.events.MouseEvent;

import spark.components.Button;
import spark.components.ViewNavigator;

public class ActionButton extends Button {

    private var actionLog:ActionLog;
    private var navigator:ViewNavigator;

    public function ActionButton(actionLog:ActionLog, navigator:ViewNavigator) {
        super();
        this.actionLog = actionLog;
        this.label = actionLog.display;
        this.navigator = navigator;
        addEventListener(MouseEvent.CLICK, onClick);
    }

    private function onClick(event:MouseEvent):void {
        if (actionLog is ActionReportLog) {
            var metadata:ReportMetadata = new ReportMetadata();
            metadata.descriptor = ActionReportLog(actionLog).insightDescriptor;
            navigator.pushView(ReportView, metadata);
        } else if (actionLog is ActionScorecardLog) {
            navigator.pushView(ScorecardView, ActionScorecardLog(actionLog).scorecardDescriptor);
        } else if (actionLog is ActionDashboardLog) {
            navigator.pushView(DashboardView, ActionDashboardLog(actionLog).dashboardDescriptor);
        }
    }
}
}
