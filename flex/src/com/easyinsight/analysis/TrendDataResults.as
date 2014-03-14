package com.easyinsight.analysis
{
import mx.collections.ArrayCollection;

[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.TrendDataResults")]
	public class TrendDataResults extends DataResults
	{
		public var trendOutcomes:ArrayCollection;
    public var nowString:String;
    public var previousString:String;

		public function TrendDataResults()
		{			
		}

	}
}