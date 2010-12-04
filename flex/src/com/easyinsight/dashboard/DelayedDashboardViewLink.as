package com.easyinsight.dashboard
{
import com.easyinsight.framework.PerspectiveInfo;
import com.easyinsight.framework.User;
import com.easyinsight.genredata.AnalyzeEvent;
	import flash.events.EventDispatcher;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	public class DelayedDashboardViewLink extends EventDispatcher
	{
		private var dashboardService:RemoteObject;
		private var dashboardID:String;
		
		public function DelayedDashboardViewLink(dashboardID:String)
		{
			this.dashboardID = dashboardID;
			this.dashboardService = new RemoteObject();
			dashboardService.destination = "dashboardService";
			dashboardService.canAccessDashboard.addEventListener(ResultEvent.RESULT, gotGoalTree);
		}
		
		public function execute():void {
            if (User.getInstance().guestUser) return;
			dashboardService.canAccessDashboard.send(dashboardID);
		}

        private function gotGoalTree(event:ResultEvent):void {
            var result:int = dashboardService.canAccessDashboard.lastResult as int;
            if (result > 0) {
                dispatchEvent(new AnalyzeEvent(new PerspectiveInfo(PerspectiveInfo.DASHBOARD_VIEW, {dashboardID: result})))
            }
        }
	}
}