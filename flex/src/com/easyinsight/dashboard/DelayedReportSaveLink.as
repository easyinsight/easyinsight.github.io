package com.easyinsight.dashboard
{
import com.easyinsight.framework.PerspectiveInfo;
import com.easyinsight.genredata.AnalyzeEvent;
import com.easyinsight.report.ReportAnalyzeSource;
import com.easyinsight.solutions.InsightDescriptor;
import com.easyinsight.util.ProgressAlert;

import flash.events.EventDispatcher;

import mx.core.Application;
import mx.core.UIComponent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class DelayedReportSaveLink extends EventDispatcher
	{
		private var reportService:RemoteObject;
		private var reportID:String;
		
		public function DelayedReportSaveLink(reportID:String)
		{
			this.reportID = reportID;
			this.reportService = new RemoteObject();
			reportService.destination = "analysisDefinition";
			reportService.retrieveFromReportLink.addEventListener(ResultEvent.RESULT, gotGoalTree);
		}
		
		public function execute():void {
            ProgressAlert.alert(UIComponent(Application.application), "Retrieving report info...", null, reportService.retrieveFromReportLink);
			reportService.retrieveFromReportLink.send(reportID);
		}

        private function gotGoalTree(event:ResultEvent):void {
            var result:DashboardInfo = reportService.retrieveFromReportLink.lastResult as DashboardInfo;
            if (result != null) {
                result.dashboardStackPositions.urlKey = reportID;
                dispatchEvent(new AnalyzeEvent(new ReportAnalyzeSource(InsightDescriptor(result.report), null, null, null, 0, null, null, null, result.dashboardStackPositions)));
            }
        }
	}
}