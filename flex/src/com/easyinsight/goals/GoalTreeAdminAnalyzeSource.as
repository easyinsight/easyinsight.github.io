package com.easyinsight.goals
{
	import com.easyinsight.FullScreenPage;
	import com.easyinsight.listing.AnalyzeSource;

	public class GoalTreeAdminAnalyzeSource implements AnalyzeSource
	{
		private var goalTreeID:int;
		
		public function GoalTreeAdminAnalyzeSource(goalTreeID:int)
		{
			this.goalTreeID = goalTreeID;
		}

		public function createAnalysisPopup():FullScreenPage
		{
			var goalTreeAdminContainer:GoalTreeAdminContainer = new GoalTreeAdminContainer();
			goalTreeAdminContainer.goalTreeID = goalTreeID;
			return goalTreeAdminContainer;
		}
		
	}
}