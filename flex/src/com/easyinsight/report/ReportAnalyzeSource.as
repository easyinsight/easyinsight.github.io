package com.easyinsight.report {
import com.easyinsight.FullScreenPage;
import com.easyinsight.analysis.EmbeddedControllerLookup;

import com.easyinsight.analysis.IEmbeddedReportController;

import com.easyinsight.listing.AnalyzeSource;
import com.easyinsight.solutions.InsightDescriptor;



import mx.collections.ArrayCollection;

public class ReportAnalyzeSource implements AnalyzeSource{

    private var insightDescriptor:InsightDescriptor;
    private var filters:ArrayCollection;
    private var installOption:Boolean;

    public function ReportAnalyzeSource(insightDescriptor:InsightDescriptor, filters:ArrayCollection = null, installOption:Boolean = false) {
        super();
        this.insightDescriptor = insightDescriptor;
        this.filters = filters;
        this.installOption = installOption;
    }

    public function createAnalysisPopup():FullScreenPage {
        var reportView:ReportView = new ReportView();
        reportView.reportID = insightDescriptor.id;
        reportView.reportType = insightDescriptor.reportType;
        reportView.dataSourceID = insightDescriptor.dataFeedID;
        reportView.parameterFilters = filters;
        reportView.showAddBar = installOption;
        reportView.showLogo = false;
        var controllerClass:Class = EmbeddedControllerLookup.controllerForType(insightDescriptor.reportType);
        var controller:IEmbeddedReportController = new controllerClass();
        reportView.viewFactory = controller.createEmbeddedView();
        return reportView;
    }
}
}