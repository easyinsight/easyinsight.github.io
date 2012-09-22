package com.easyinsight.analysis
{
import mx.collections.ArrayCollection;

[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.TreeDataResults")]
	public class TreeDataResults extends DataResults
	{
		public var treeRows:ArrayCollection;

		public function TreeDataResults()
		{			
		}

	}
}