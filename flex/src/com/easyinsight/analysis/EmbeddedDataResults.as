package com.easyinsight.analysis
{
import com.easyinsight.datasources.DataSourceInfo;

import mx.collections.ArrayCollection;

[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.EmbeddedDataResults")]
	public class EmbeddedDataResults extends EmbeddedResults
	{
		public var headers:Array;
		public var rows:Array;

		public function EmbeddedDataResults()
			{
			super();
		}

	}
}