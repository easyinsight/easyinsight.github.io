package com.easyinsight.report {
import com.easyinsight.framework.PerspectiveInfo;

public class MultiReportAnalyzeSource extends PerspectiveInfo {

    public function MultiReportAnalyzeSource(dataSourceID:int, dataSourceName:String = null) {
        super(PerspectiveInfo.MULTI_REPORT_VIEW, new Object());
        properties.dataSourceID = dataSourceID;
        properties.dataSourceName = dataSourceName;
    }
}
}