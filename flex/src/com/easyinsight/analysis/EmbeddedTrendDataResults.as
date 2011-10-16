package com.easyinsight.analysis
{
import mx.collections.ArrayCollection;

[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.EmbeddedTrendDataResults")]
	public class EmbeddedTrendDataResults extends EmbeddedResults
	{
		public var trendOutcomes:ArrayCollection;

		public function EmbeddedTrendDataResults()
		{			
		}

	}
}