package com.easyinsight.goals
{
import com.easyinsight.framework.PerspectiveInfo;
import com.easyinsight.framework.User;
import com.easyinsight.genredata.AnalyzeEvent;
	import flash.events.EventDispatcher;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	public class DelayedScorecardAdminLink extends EventDispatcher
	{
		private var scorecardService:RemoteObject;
		private var scorecardID:String;
		
		public function DelayedScorecardAdminLink(dashboardID:String)
		{
			this.scorecardID = dashboardID;
			this.scorecardService = new RemoteObject();
			scorecardService.destination = "scorecardService";
			scorecardService.canAccessScorecard.addEventListener(ResultEvent.RESULT, gotGoalTree);
		}
		
		public function execute():void {
            if (User.getInstance().guestUser) return;
			scorecardService.canAccessScorecard.send(scorecardID);
		}

        private function gotGoalTree(event:ResultEvent):void {
            var result:int = scorecardService.canAccessScorecard.lastResult as int;
            if (result > 0) {
                dispatchEvent(new AnalyzeEvent(new PerspectiveInfo(PerspectiveInfo.SCORECARD_EDITOR, {scorecardID: result})))
            }
        }
	}
}