package com.easyinsight.goals
{
    import com.easyinsight.genredata.AnalyzeEvent;
	import flash.events.EventDispatcher;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	public class DelayedGoalAdminLink extends EventDispatcher
	{
		private var goalService:RemoteObject;
		private var goalTreeID:String;
		
		public function DelayedGoalAdminLink(goalTreeID:String)
		{
			this.goalTreeID = goalTreeID;
			this.goalService = new RemoteObject();
			goalService.destination = "goalService";
			goalService.canAccessGoalTree.addEventListener(ResultEvent.RESULT, gotGoalTree);
		}
		
		public function execute():void {
			goalService.canAccessGoalTree.send(goalTreeID);
		}

        private function gotGoalTree(event:ResultEvent):void {
            var result:int = goalService.canAccessGoalTree.lastResult as int;
            if (result > 0) {
                dispatchEvent(new AnalyzeEvent(new GoalTreeAdminAnalyzeSource(result)));
            }
        }
	}
}