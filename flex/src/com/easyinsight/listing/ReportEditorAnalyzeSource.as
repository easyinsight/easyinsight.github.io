package com.easyinsight.listing
{
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.FeedMetadata;
import com.easyinsight.framework.PerspectiveInfo;

	public class ReportEditorAnalyzeSource extends PerspectiveInfo
	{
		
		public function ReportEditorAnalyzeSource(report:AnalysisDefinition, selectedTabIndex:int = 0, feedMetadata:FeedMetadata = null, openState:Object = null) {
            super(PerspectiveInfo.REPORT_EDITOR, new Object());
			properties.analysisDefinition = report;
			properties.dataSourceID = report.dataFeedID;
            properties.selectedTabIndex = selectedTabIndex;
            properties.feedMetadata = feedMetadata;
            properties.openState = openState;
		}
	}
}