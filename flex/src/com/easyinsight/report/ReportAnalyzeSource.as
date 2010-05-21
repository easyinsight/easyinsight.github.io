package com.easyinsight.report {
import com.easyinsight.FullScreenPage;
import com.easyinsight.analysis.EmbeddedControllerLookup;

import com.easyinsight.analysis.IEmbeddedReportController;

import com.easyinsight.listing.AnalyzeSource;
import com.easyinsight.solutions.InsightDescriptor;



import mx.collections.ArrayCollection;

public class ReportAnalyzeSource implements AnalyzeSource{

    public static const ORIGIN_MY_DATA:int = 1;
    public static const ORIGIN_EXCHANGE:int = 2;

    public var insightDescriptor:InsightDescriptor;
    private var filters:ArrayCollection;
    private var installOption:Boolean;
    private var origin:int;
    private var originalReportID:int;
    private var originScore:Number;
    private var templateUrlKey:String;

    public function ReportAnalyzeSource(insightDescriptor:InsightDescriptor, filters:ArrayCollection = null, installOption:Boolean = false, origin:int = 0,
            originalReportID:int = 0, originScore:Number = 0, templateUrlKey:String = null) {
        super();
        this.insightDescriptor = insightDescriptor;
        this.filters = filters;
        this.installOption = installOption;
        this.origin = origin;
        this.originalReportID = originalReportID;
        this.originScore = originScore;
        this.templateUrlKey = templateUrlKey;
    }

    public function createAnalysisPopup():FullScreenPage {
        var reportView:ReportView = new ReportView();
        reportView.reportID = insightDescriptor.id;
        reportView.reportType = insightDescriptor.reportType;
        reportView.dataSourceID = insightDescriptor.dataFeedID;
        reportView.parameterFilters = filters;
        reportView.showAddBar = installOption;
        reportView.origin = origin;
        reportView.showLogo = false;
        reportView.originReportID = originalReportID;
        reportView.originReportScore = originScore;
        reportView.templateUrlKey = templateUrlKey;
        var controllerClass:Class = EmbeddedControllerLookup.controllerForType(insightDescriptor.reportType);
        var controller:IEmbeddedReportController = new controllerClass();
        reportView.viewFactory = controller.createEmbeddedView();
        return reportView;
    }
}
}