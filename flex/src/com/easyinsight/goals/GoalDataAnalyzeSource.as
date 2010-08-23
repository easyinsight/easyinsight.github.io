package com.easyinsight.goals {
import com.easyinsight.framework.PerspectiveInfo;

public class GoalDataAnalyzeSource extends PerspectiveInfo {

		public function GoalDataAnalyzeSource(goalTreeID:int)
		{
            super(PerspectiveInfo.KPI_TREE_VIEW, new Object());
			properties.goalTreeID = goalTreeID;
		}

}
}