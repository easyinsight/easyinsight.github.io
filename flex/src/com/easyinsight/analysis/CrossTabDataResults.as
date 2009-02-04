package com.easyinsight.analysis
{
	import mx.collections.ArrayCollection;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.webservice.google.CrossTabDataResults")]
	public class CrossTabDataResults
	{
		public var results:ArrayCollection;
        public var invalidAnalysisItemIDs:ArrayCollection;
        public var feedMetadata:FeedMetadata;
		public function CrossTabDataResults()
		{			
		}

	}
}