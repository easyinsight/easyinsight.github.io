package com.easyinsight.listing
{
import com.easyinsight.framework.PerspectiveInfo;
import com.easyinsight.solutions.InsightDescriptor;

import mx.collections.ArrayCollection;

	public class AnalysisDefinitionAnalyzeSource extends PerspectiveInfo
	{
		
		public function AnalysisDefinitionAnalyzeSource(insightDescriptor:InsightDescriptor, filters:ArrayCollection = null) {
            super(PerspectiveInfo.REPORT_EDITOR, new Object());
			properties.dataSourceID = insightDescriptor.dataFeedID;
            properties.filterOverrides = filters;
            properties.reportID = insightDescriptor.id;
		}
	}
}