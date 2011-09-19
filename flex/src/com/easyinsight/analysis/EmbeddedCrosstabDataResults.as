package com.easyinsight.analysis
{
import mx.collections.ArrayCollection;

[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.EmbeddedCrosstabDataResults")]
	public class EmbeddedCrosstabDataResults extends EmbeddedResults
	{
		public var dataSet:ArrayCollection;
        public var columnCount:int;

		public function EmbeddedCrosstabDataResults()
		{			
		}

	}
}