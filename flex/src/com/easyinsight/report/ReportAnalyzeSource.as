package com.easyinsight.report {
import com.easyinsight.analysis.EmbeddedControllerLookup;
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;
import com.easyinsight.framework.ModuleAnalyzeSource;
import com.easyinsight.solutions.InsightDescriptor;

import flash.display.DisplayObject;

import mx.collections.ArrayCollection;

public class ReportAnalyzeSource extends ModuleAnalyzeSource{

    private var insightDescriptor:InsightDescriptor;
    private var filters:ArrayCollection;

    public function ReportAnalyzeSource(insightDescriptor:InsightDescriptor, filters:ArrayCollection = null) {
        super();
        this.insightDescriptor = insightDescriptor;
        this.filters = filters;
    }

    override public function createDirect():DisplayObject {
        var reportView:ReportView = new ReportView();
        reportView.reportID = insightDescriptor.id;
        reportView.reportType = insightDescriptor.reportType;
        reportView.dataSourceID = insightDescriptor.dataFeedID;
        reportView.parameterFilters = filters;
        reportView.showLogo = false;
        var controllerClass:Class = EmbeddedControllerLookup.controllerForType(insightDescriptor.reportType);
        var controller:IEmbeddedReportController = new controllerClass();
        reportView.viewFactory = controller.createEmbeddedView();
        return reportView;
    }
}
}