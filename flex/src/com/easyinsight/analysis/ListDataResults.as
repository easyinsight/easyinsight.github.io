package com.easyinsight.analysis
{
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.ListDataResults")]
	public class ListDataResults extends DataResults
	{
		public var headers:Array;
		public var rows:Array;

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