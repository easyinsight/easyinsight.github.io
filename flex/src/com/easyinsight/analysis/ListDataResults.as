package com.easyinsight.analysis
{
	import mx.collections.ArrayCollection;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.webservice.google.ListDataResults")]
	public class ListDataResults
	{
		public var headers:Array;
		public var rows:Array;
		public var credentialFailures:ArrayCollection;
		public var headerMetadata:Array;
		public var limitedResults:Boolean;
		public var limitResults:int;
		public var maxResults:int;
		
		public function ListDataResults()
			{
			super();
		}

	}
}