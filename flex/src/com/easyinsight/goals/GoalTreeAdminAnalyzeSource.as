package com.easyinsight.goals
{
	import com.easyinsight.FullScreenPage;
	import com.easyinsight.listing.AnalyzeSource;

	public class GoalTreeAdminAnalyzeSource implements AnalyzeSource
	{
		private var goalTree:GoalTree;
		
		public function GoalTreeAdminAnalyzeSource(goalTree:GoalTree)
		{
			this.goalTree = goalTree;
		}

		public function createAnalysisPopup():FullScreenPage
		{
			var goalTreeAdminContainer:GoalTreeAdminContainer = new GoalTreeAdminContainer();
			goalTreeAdminContainer.goalTree = goalTree;
			return goalTreeAdminContainer;
		}
		
	}
}