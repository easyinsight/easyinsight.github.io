package com.easyinsight.analysis
{
	import mx.collections.ArrayCollection;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.datafeeds.InitialAnalysis")]
	public class InitialAnalysis
	{
		public var filterDefinitions:ArrayCollection;
		
		public function InitialAnalysis()
		{
		}	
	}
}