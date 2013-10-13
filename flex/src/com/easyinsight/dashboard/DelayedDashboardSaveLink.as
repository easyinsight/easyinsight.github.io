package com.easyinsight.dashboard
{
import com.easyinsight.framework.PerspectiveInfo;
import com.easyinsight.framework.User;
import com.easyinsight.genredata.AnalyzeEvent;
import com.easyinsight.util.ProgressAlert;

import flash.events.EventDispatcher;

import mx.core.Application;
import mx.core.UIComponent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class DelayedDashboardSaveLink extends EventDispatcher
	{
		private var dashboardService:RemoteObject;
		private var dashboardID:String;
		
		public function DelayedDashboardSaveLink(dashboardID:String)
		{
			this.dashboardID = dashboardID;
			this.dashboardService = new RemoteObject();
			dashboardService.destination = "dashboardService";
			dashboardService.retrieveFromDashboardLink.addEventListener(ResultEvent.RESULT, gotGoalTree);
		}
		
		public function execute():void {
            ProgressAlert.alert(UIComponent(Application.application), "Retrieving dashboard info...", null, dashboardService.retrieveFromDashboardLink);
			dashboardService.retrieveFromDashboardLink.send(dashboardID);
		}

        private function gotGoalTree(event:ResultEvent):void {
            var result:DashboardInfo = dashboardService.retrieveFromDashboardLink.lastResult as DashboardInfo;
            if (result != null) {
                result.dashboardStackPositions.urlKey = dashboardID;
                dispatchEvent(new AnalyzeEvent(new PerspectiveInfo(PerspectiveInfo.DASHBOARD_VIEW, {dashboardID: result.dashboardID, dashboardStackPositions: result.dashboardStackPositions})))
            }
        }
	}
}