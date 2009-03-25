package com.easyinsight.analysis.split
{
import com.easyinsight.analysis.*;
	public class SplitDefinition extends AnalysisDefinition
	{
		public var sourceDefinition:AnalysisDefinition;
		public var targetDefinition:AnalysisDefinition;
		
		public function SplitDefinition()
		{
			super();
		}
		
		override public function getDataFeedType():String {
			return "Split";
		}		
	}
}