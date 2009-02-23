package com.easyinsight.goals {
import com.easyinsight.FullScreenPage;
import com.easyinsight.listing.AnalyzeSource;
public class GoalDetailAnalyzeSource implements AnalyzeSource{

    private var goalNodeData:GoalTreeNodeData;

    public function GoalDetailAnalyzeSource(val:GoalTreeNodeData) {
        super();
        this.goalNodeData = val;
    }

    public function createAnalysisPopup():FullScreenPage {
        var goalDetailScreen:GoalDetailFullScreen = new GoalDetailFullScreen();
        goalDetailScreen.goalTreeNodeData = goalNodeData;
        return goalDetailScreen;
    }
}
}