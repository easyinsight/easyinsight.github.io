package com.easyinsight.report {
import com.easyinsight.analysis.EmbeddedControllerLookup;
import com.easyinsight.analysis.IEmbeddedReportController;
import com.easyinsight.framework.PerspectiveInfo;
import com.easyinsight.solutions.InsightDescriptor;

import mx.collections.ArrayCollection;

public class ReportPerspectiveInfo extends PerspectiveInfo {
    
    public function ReportPerspectiveInfo(insightDescriptor:InsightDescriptor, filters:ArrayCollection = null, installOption:Boolean = false, origin:int = 0,
            originalReportID:int = 0, originScore:Number = 0, templateUrlKey:String = null) {
        super(PerspectiveInfo.REPORT_EDITOR);
        var properties:Object = new Object();
        properties.reportID = insightDescriptor.id;
        properties.reportType = insightDescriptor.reportType;
        properties.dataSourceID = insightDescriptor.dataFeedID;
        properties.parameterFilters = filters;
        properties.showAddBar = installOption;
        properties.origin = origin;
        properties.showLogo = false;
        properties.originReportID = originalReportID;
        properties.originReportScore = originScore;
        properties.templateUrlKey = templateUrlKey;
        var controllerClass:Class = EmbeddedControllerLookup.controllerForType(insightDescriptor.reportType);
        var controller:IEmbeddedReportController = new controllerClass();
        properties.viewFactory = controller.createEmbeddedView();
        this.properties = properties;
    }
}
}