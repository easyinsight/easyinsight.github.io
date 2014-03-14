package com.easyinsight.analysis
{
import mx.collections.ArrayCollection;

[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.EmbeddedTrendDataResults")]
	public class EmbeddedTrendDataResults extends EmbeddedResults
	{
		public var trendOutcomes:ArrayCollection;
    public var nowString:String;
    public var previousString:String;

		public function EmbeddedTrendDataResults()
		{			
		}

	}
}