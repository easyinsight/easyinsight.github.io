package com.easyinsight.goals {
import com.easyinsight.listing.AnalyzeSource;
import com.easyinsight.FullScreenPage;
public class GoalDataAnalyzeSource implements AnalyzeSource
	{
		private var _goalTreeID:int;

		public function GoalDataAnalyzeSource(goalTreeID:int)
		{
			this._goalTreeID = goalTreeID;
		}


    public function get goalTreeID():int {
        return _goalTreeID;
    }

    public function createAnalysisPopup():FullScreenPage
		{
			var goalTreeAdminContainer:GoalTreeViewContainer = new GoalTreeViewContainer();
			goalTreeAdminContainer.goalTreeID = goalTreeID;
			return goalTreeAdminContainer;
		}

}
}