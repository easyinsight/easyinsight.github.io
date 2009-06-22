package com.easyinsight.report {
import com.easyinsight.framework.ModuleAnalyzeSource;
import com.easyinsight.solutions.InsightDescriptor;

import flash.display.DisplayObject;

public class ReportAnalyzeSource extends ModuleAnalyzeSource{

    private var insightDescriptor:InsightDescriptor;

    public function ReportAnalyzeSource(insightDescriptor:InsightDescriptor) {
        super();
        this.insightDescriptor = insightDescriptor;
    }

    override public function createDirect():DisplayObject {
        var reportView:ReportView = new ReportView();
        reportView.reportID = insightDescriptor.id;
        reportView.reportType = insightDescriptor.reportType;
        reportView.dataSourceID = insightDescriptor.dataFeedID;
        return reportView;
    }
}
}