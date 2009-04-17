package com.easyinsight.goals {
import com.easyinsight.listing.AnalyzeSource;
import com.easyinsight.FullScreenPage;
public class GoalDataAnalyzeSource implements AnalyzeSource
	{
		private var goalTreeID:int;

		public function GoalDataAnalyzeSource(goalTreeID:int)
		{
			this.goalTreeID = goalTreeID;
		}

		public function createAnalysisPopup():FullScreenPage
		{
			var goalTreeAdminContainer:GoalTreeViewContainer = new GoalTreeViewContainer();
			goalTreeAdminContainer.goalTreeID = goalTreeID;
			return goalTreeAdminContainer;
		}

}
}