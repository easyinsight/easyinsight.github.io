package com.easyinsight.listing
{
import com.easyinsight.framework.PerspectiveInfo;

	public class DescriptorAnalyzeSource extends PerspectiveInfo
	{
		public function DescriptorAnalyzeSource(dataFeedID:int)
		{
			super(PerspectiveInfo.REPORT_EDITOR, new Object());
			properties.dataSourceID = dataFeedID;
		}
	}
}