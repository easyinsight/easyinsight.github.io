package com.easyinsight.report {
import com.easyinsight.analysis.EmbeddedControllerLookup;

import com.easyinsight.analysis.IEmbeddedReportController;
import com.easyinsight.framework.PerspectiveInfo;
import com.easyinsight.solutions.InsightDescriptor;



import mx.collections.ArrayCollection;

public class ReportAnalyzeSource extends PerspectiveInfo {

    public function ReportAnalyzeSource(insightDescriptor:InsightDescriptor, filters:ArrayCollection = null, installOption:Boolean = false, origin:int = 0,
            originalReportID:int = 0, installs:int = 0, templateUrlKey:String = null) {
        super(PerspectiveInfo.REPORT_VIEW);
        var properties:Object = new Object();
        properties.reportID = insightDescriptor.id;
        properties.reportType = insightDescriptor.reportType;
        properties.dataSourceID = insightDescriptor.dataFeedID;
        properties.parameterFilters = filters;
        properties.showAddBar = installOption;
        properties.originReportID = originalReportID;
        properties.installs = installs;
        properties.templateUrlKey = templateUrlKey;
        var controllerClass:Class = EmbeddedControllerLookup.controllerForType(insightDescriptor.reportType);
        var controller:IEmbeddedReportController = new controllerClass();
        properties.viewFactory = controller.createEmbeddedView();
        this.properties = properties;
    }
}
}