package com.easyinsight.goals
{
import com.easyinsight.framework.PerspectiveInfo;


public class GoalTreeAdminAnalyzeSource extends PerspectiveInfo
	{
		
		public function GoalTreeAdminAnalyzeSource(goalTreeID:int, dataSourceID:int)
		{
            super(PerspectiveInfo.KPI_TREE_ADMIN, new Object());
			properties.goalTreeID = goalTreeID;
            properties.startDataSourceID = dataSourceID;
		}
		
	}
}