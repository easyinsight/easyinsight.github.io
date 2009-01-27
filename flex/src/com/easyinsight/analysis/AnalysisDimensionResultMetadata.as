package com.easyinsight.analysis
{
	import mx.collections.ArrayCollection;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.AnalysisDimensionResultMetadata")]
	public class AnalysisDimensionResultMetadata extends AnalysisItemResultMetadata
	{
		public var values:ArrayCollection;
		
		public function AnalysisDimensionResultMetadata()
		{
			super();
		}
		
	}
}