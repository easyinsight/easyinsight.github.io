package com.easyinsight.report {
import com.easyinsight.quicksearch.EIDescriptor;
import com.easyinsight.solutions.InsightDescriptor;
import com.easyinsight.util.IAsyncScreen;
import com.easyinsight.util.IAsyncScreenFactory;

public class ReportScreenFactory implements IAsyncScreenFactory{
    public function ReportScreenFactory() {
        super();
    }

    public function createScreen(descriptor:EIDescriptor):IAsyncScreen {
        if (descriptor.getType() == EIDescriptor.REPORT) {
            var insightDescriptor:InsightDescriptor = descriptor as InsightDescriptor;
            var reportView:ReportView = new ReportView();
            reportView.reportID = insightDescriptor.id;
            reportView.reportType = insightDescriptor.reportType;
            reportView.dataSourceID = insightDescriptor.dataFeedID;
            reportView.embedded = true;
            return reportView;
        } else {
            return null;
        }
    }
}
}