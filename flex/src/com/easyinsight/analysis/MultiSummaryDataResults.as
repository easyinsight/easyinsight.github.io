package com.easyinsight.analysis
{
import mx.collections.ArrayCollection;

[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.MultiSummaryDataResults")]
	public class MultiSummaryDataResults extends DataResults
	{
		public var treeRows:ArrayCollection;

		public function MultiSummaryDataResults()
		{			
		}

	}
}