package com.easyinsight.dashboard
{
import com.easyinsight.genredata.AnalyzeEvent;
import com.easyinsight.report.ReportAnalyzeSource;
import com.easyinsight.report.SavedConfiguration;
import com.easyinsight.solutions.InsightDescriptor;
import com.easyinsight.util.ProgressAlert;

import flash.events.EventDispatcher;

import mx.core.Application;
import mx.core.UIComponent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class DelayedReportConfigurationLink extends EventDispatcher
	{
		private var reportService:RemoteObject;
		private var reportID:String;
		
		public function DelayedReportConfigurationLink(reportID:String)
		{
			this.reportID = reportID;
			this.reportService = new RemoteObject();
			reportService.destination = "dashboardService";
			reportService.getConfigurationForReport.addEventListener(ResultEvent.RESULT, gotGoalTree);
		}
		
		public function execute():void {
            ProgressAlert.alert(UIComponent(Application.application), "Retrieving report info...", null, reportService.getConfigurationForReport);
			reportService.getConfigurationForReport.send(reportID);
		}

        private function gotGoalTree(event:ResultEvent):void {
            var result:DashboardInfo = reportService.getConfigurationForReport.lastResult as DashboardInfo;
            if (result != null) {
                /*result.savedConfiguration..urlKey = reportID;*/
                dispatchEvent(new AnalyzeEvent(new ReportAnalyzeSource(InsightDescriptor(result.report), null, null, null,
                        result.savedConfiguration.dashboardStackPositions, result.savedConfiguration)));
            }
        }
	}
}