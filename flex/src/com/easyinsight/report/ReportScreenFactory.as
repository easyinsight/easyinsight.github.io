package com.easyinsight.report {
import com.easyinsight.analysis.EmbeddedControllerLookup;
import com.easyinsight.analysis.IEmbeddedReportController;
import com.easyinsight.quicksearch.EIDescriptor;
import com.easyinsight.solutions.EmptyReportDescriptor;
import com.easyinsight.solutions.InsightDescriptor;
import com.easyinsight.util.IAsyncScreen;
import com.easyinsight.util.IAsyncScreenFactory;

public class ReportScreenFactory implements IAsyncScreenFactory{

    private var _reportNavHandler:Function;

    public function ReportScreenFactory() {
        super();
    }

    public function set reportNavHandler(value:Function):void {
        _reportNavHandler = value;
    }

    public function createScreen(descriptor:EIDescriptor):IAsyncScreen {
        if  (descriptor.getType() == EIDescriptor.EMPTY) {
            var desc:EmptyReportDescriptor = descriptor as EmptyReportDescriptor;
            var view:EmptyReportView = new EmptyReportView();
            view.feedID = desc.feedID;
            view.feedName = desc.feedName;
            return view;
        }
        if (descriptor.getType() == EIDescriptor.REPORT) {
            var insightDescriptor:InsightDescriptor = descriptor as InsightDescriptor;
            var reportView:ReportView = new ReportView();
            reportView.reportID = insightDescriptor.id;
            reportView.reportNavHandler = _reportNavHandler;
            reportView.reportType = insightDescriptor.reportType;
            reportView.dataSourceID = insightDescriptor.dataFeedID;
            reportView.showBack = false;
            reportView.updateURL = false;
            var controllerClass:Class = EmbeddedControllerLookup.controllerForType(insightDescriptor.reportType);
            var controller:IEmbeddedReportController = new controllerClass();
            reportView.viewFactory = controller.createEmbeddedView();
            return reportView;
        }
        return null;
    }
}
}