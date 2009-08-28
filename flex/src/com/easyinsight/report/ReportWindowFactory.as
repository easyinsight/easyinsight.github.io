package com.easyinsight.report {
import com.easyinsight.analysis.EmbeddedControllerLookup;
import com.easyinsight.analysis.IEmbeddedReportController;
import com.easyinsight.goals.GoalHistoryDescriptor;
import com.easyinsight.goals.GoalHistoryView;
import com.easyinsight.goals.GoalTreeViewContainer;
import com.easyinsight.quicksearch.EIDescriptor;
import com.easyinsight.solutions.InsightDescriptor;
import com.easyinsight.util.IAsyncScreen;
import com.easyinsight.util.IAsyncScreenFactory;

public class ReportWindowFactory implements IAsyncScreenFactory{
    public function ReportWindowFactory() {
        super();
    }

    public function createScreen(descriptor:EIDescriptor):IAsyncScreen {
        if (descriptor.getType() == EIDescriptor.REPORT) {
            var insightDescriptor:InsightDescriptor = descriptor as InsightDescriptor;
            var reportView:ReportView = new ReportView();
            reportView.reportID = insightDescriptor.id;
            reportView.reportType = insightDescriptor.reportType;
            reportView.dataSourceID = insightDescriptor.dataFeedID;
            reportView.showBack = false;
            reportView.showBookmark = false;
            reportView.showFullScreen = false;
            reportView.showFilterButton = false;
            reportView.showExport = false;
            reportView.showMetadata = false;
            reportView.showReportEditor = false;
            reportView.showRefresh = false;
            reportView.showBrowser = false;
            reportView.showBack = false;
            reportView.showLogo = false;
            var controllerClass:Class = EmbeddedControllerLookup.controllerForType(insightDescriptor.reportType);
            var controller:IEmbeddedReportController = new controllerClass();
            reportView.viewFactory = controller.createEmbeddedView();
            return reportView;
        } else if (descriptor.getType() == EIDescriptor.GOAL_HISTORY) {
            var histDescriptor:GoalHistoryDescriptor = descriptor as GoalHistoryDescriptor;
            var historyView:GoalHistoryView = new GoalHistoryView();
            historyView.goalID = descriptor.id;
            historyView.dataSourceID = histDescriptor.dataSourceID;
            historyView.dataSourceName = histDescriptor.dataSourceName;
            return historyView;
        } else {
            var goalView:GoalTreeViewContainer = new GoalTreeViewContainer();
            goalView.goalTreeID = descriptor.id;
            goalView.embedded = true;
            return goalView;
        }
    }
}
}