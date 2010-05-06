package com.easyinsight.dashboard {
import com.easyinsight.analysis.AirViewFactory;
import com.easyinsight.analysis.EmbeddedControllerLookup;
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;
import com.easyinsight.genredata.AnalyzeEvent;
import com.easyinsight.goals.GoalTreeViewContainer;
import com.easyinsight.quicksearch.EIDescriptor;
import com.easyinsight.report.ReportView;
import com.easyinsight.solutions.InsightDescriptor;
import com.easyinsight.util.IAsyncScreen;
import com.easyinsight.util.IAsyncScreenFactory;

public class AirScreenFactory implements IAsyncScreenFactory{

    private var adapter:AnalyzeEventAdapter;

    public function AirScreenFactory(adapter:AnalyzeEventAdapter) {
        super();
        this.adapter = adapter;
    }

    public function createScreen(descriptor:EIDescriptor):IAsyncScreen {
        if (descriptor.getType() == EIDescriptor.REPORT) {
            var insightDescriptor:InsightDescriptor = descriptor as InsightDescriptor;
            var reportView:ReportView = new ReportView();
            reportView.showBack = false;
            reportView.showBookmark = false;
            reportView.showFullScreen = false;
            reportView.showExport = false;
            reportView.showMetadata = false;
            reportView.showReportEditor = false;
            reportView.reportID = insightDescriptor.id;
            reportView.reportType = insightDescriptor.reportType;
            reportView.dataSourceID = insightDescriptor.dataFeedID;
            var controllerClass:Class = EmbeddedControllerLookup.controllerForType(insightDescriptor.reportType);
            var controller:IEmbeddedReportController = new controllerClass();
            var dataViewFactory:EmbeddedViewFactory = controller.createEmbeddedView();
            var airViewFactory:AirViewFactory = new AirViewFactory();
            airViewFactory.dataSourceID = insightDescriptor.dataFeedID;
            airViewFactory.reportType = insightDescriptor.reportType;
            airViewFactory.reportID = insightDescriptor.id;
            reportView.viewFactory = airViewFactory;
            return reportView;
        } else if (descriptor.getType() == EIDescriptor.GOAL_TREE) {
            var goalView:GoalTreeViewContainer = new GoalTreeViewContainer();
            goalView.goalTreeID = descriptor.id;
            goalView.embedded = true;
            goalView.showBackButton = false;
            goalView.showEditButton = false;
            goalView.showFullScreen = false;
            goalView.addEventListener(AnalyzeEvent.ANALYZE, onAnalyze);
            return goalView;
        } else if (descriptor.getType() == EIDescriptor.AIR_INTRO) {
            return new DefaultAirScreen();
        } else {
            return null;
        }
    }

    private function onAnalyze(event:AnalyzeEvent):void {
        adapter.translateAnalyze(event);
    }
}
}