package com.easyinsight.dashboard
{
import com.easyinsight.framework.PerspectiveInfo;
import com.easyinsight.genredata.AnalyzeEvent;
import com.easyinsight.util.ProgressAlert;

import flash.events.EventDispatcher;

import mx.core.Application;
import mx.core.UIComponent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class DelayedDashboardConfigurationLink extends EventDispatcher
	{
		private var reportService:RemoteObject;
		private var reportID:String;
		
		public function DelayedDashboardConfigurationLink(reportID:String)
		{
			this.reportID = reportID;
			this.reportService = new RemoteObject();
			reportService.destination = "dashboardService";
			reportService.getConfigurationForDashboard.addEventListener(ResultEvent.RESULT, gotGoalTree);
		}
		
		public function execute():void {
            ProgressAlert.alert(UIComponent(Application.application), "Retrieving dashboard info...", null, reportService.getConfigurationForDashboard);
			reportService.getConfigurationForDashboard.send(reportID);
		}

        private function gotGoalTree(event:ResultEvent):void {
            var result:DashboardInfo = reportService.getConfigurationForDashboard.lastResult as DashboardInfo;
            if (result != null) {
                dispatchEvent(new AnalyzeEvent(new PerspectiveInfo(PerspectiveInfo.DASHBOARD_VIEW, { dashboardID: result.dashboardID, dashboardStackPositions: result.savedConfiguration.dashboardStackPositions,
                    selectedConfiguration: result.savedConfiguration })));
            }
        }
	}
}