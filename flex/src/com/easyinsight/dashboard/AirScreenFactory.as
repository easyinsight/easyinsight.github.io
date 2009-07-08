package com.easyinsight.dashboard {
import com.easyinsight.goals.MyGoalsGrid;
import com.easyinsight.goals.GoalTreeViewContainer;
import com.easyinsight.quicksearch.EIDescriptor;
import com.easyinsight.solutions.InsightDescriptor;
import com.easyinsight.util.IAsyncScreen;
import com.easyinsight.util.IAsyncScreenFactory;

public class AirScreenFactory implements IAsyncScreenFactory{
    public function AirScreenFactory() {
        super();
    }

    public function createScreen(descriptor:EIDescriptor):IAsyncScreen {
        if (descriptor.getType() == EIDescriptor.REPORT) {
            var insightDescriptor:InsightDescriptor = descriptor as InsightDescriptor;
            var reportView:AirViewPanel = new AirViewPanel();
            reportView.analysisID = insightDescriptor.id;
            reportView.reportType = insightDescriptor.reportType;
            reportView.dataSourceID = insightDescriptor.dataFeedID;
            return reportView;
        } else if (descriptor.getType() == EIDescriptor.GOAL_TREE) {
            var goalView:GoalTreeViewContainer = new GoalTreeViewContainer();
            goalView.goalTreeID = descriptor.id;
            goalView.embedded = true;
            return goalView;
        } else if (descriptor.getType() == EIDescriptor.MY_GOALS) {
            return new MyGoalsGrid();
        } else if (descriptor.getType() == EIDescriptor.AIR_INTRO) {
            return new DefaultAirScreen();
        } else {
            return null;
        }
    }
}
}