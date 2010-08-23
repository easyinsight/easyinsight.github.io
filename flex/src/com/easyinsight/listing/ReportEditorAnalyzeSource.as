package com.easyinsight.listing
{
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.framework.PerspectiveInfo;

	public class ReportEditorAnalyzeSource extends PerspectiveInfo
	{
		
		public function ReportEditorAnalyzeSource(report:AnalysisDefinition) {
            super(PerspectiveInfo.REPORT_EDITOR, new Object());
			properties.analysisDefinition = report;
			properties.dataSourceID = report.dataFeedID;
		}
	}
}