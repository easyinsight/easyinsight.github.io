package com.easyinsight.analysis
{
import mx.collections.ArrayCollection;

[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.EmbeddedTreeDataResults")]
	public class EmbeddedTreeDataResults extends EmbeddedResults
	{
		public var treeRows:ArrayCollection;

		public function EmbeddedTreeDataResults()
		{			
		}

	}
}