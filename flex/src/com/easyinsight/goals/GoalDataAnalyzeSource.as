package com.easyinsight.goals {
import com.easyinsight.listing.AnalyzeSource;
import com.easyinsight.FullScreenPage;
public class GoalDataAnalyzeSource implements AnalyzeSource
	{
		private var goalTree:GoalTree;

		public function GoalDataAnalyzeSource(goalTree:GoalTree)
		{
			this.goalTree = goalTree;
		}

		public function createAnalysisPopup():FullScreenPage
		{
			var goalTreeAdminContainer:GoalTreeViewContainer = new GoalTreeViewContainer();
			goalTreeAdminContainer.goalTree = goalTree;
			return goalTreeAdminContainer;
		}

}
}