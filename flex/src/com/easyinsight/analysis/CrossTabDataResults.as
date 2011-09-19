package com.easyinsight.analysis
{
import mx.collections.ArrayCollection;

[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.CrossTabDataResults")]
	public class CrossTabDataResults extends DataResults
	{
		public var dataSet:ArrayCollection;
        public var columnCount:int;

		public function CrossTabDataResults()
		{			
		}

	}
}