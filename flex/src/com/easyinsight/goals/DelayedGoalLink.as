package com.easyinsight.goals
{
    import com.easyinsight.genredata.AnalyzeEvent;
	import flash.events.EventDispatcher;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	public class DelayedGoalLink extends EventDispatcher
	{
		private var goalService:RemoteObject;
		private var goalTreeID:int;
		
		public function DelayedGoalLink(goalTreeID:int)
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
            var result:Boolean = goalService.canAccessGoalTree.lastResult as Boolean;
            if (result) {                                 
                dispatchEvent(new AnalyzeEvent(new GoalDataAnalyzeSource(goalTreeID)));
            }
        }
	}
}