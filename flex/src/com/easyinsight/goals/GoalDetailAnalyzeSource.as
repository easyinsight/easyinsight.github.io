package com.easyinsight.goals {
import com.easyinsight.framework.PerspectiveInfo;

public class GoalDetailAnalyzeSource extends PerspectiveInfo {

    public function GoalDetailAnalyzeSource(val:GoalTreeNode) {
        super(PerspectiveInfo.GOAL_DETAIL, new Object());
        properties.goalNodeData = val;
    }
}
}