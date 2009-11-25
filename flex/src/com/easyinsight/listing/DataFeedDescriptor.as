package com.easyinsight.listing
{
	import com.easyinsight.analysis.AnalysisDefinition;
	import com.easyinsight.customupload.UploadPolicy;
	
	import mx.collections.ArrayCollection;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.datafeeds.FeedDescriptor")]
	public class DataFeedDescriptor
	{
		public static const GOOGLE:int = 1;
		public static const STATIC:int = 2;
		public static const COMPOSITE:int = 3;
		public static const SALESFORCE:int = 4;
		public static const ANALYSIS:int = 5;
		public static const SALESFORCE_SUB:int = 6;
		public static const EMPTY:int = 8;
		public static const JIRA:int = 9;
		public static const BASECAMP:int = 10;
        public static const GNIP:int = 12;
        public static const GOOGLE_ANALYTICS:int = 13;

		public static const OWNER:int = 1;
		public static const SUBSCRIBER:int = 3;
		
		public var name:String;
		public var dataFeedID:int;		
		public var genre:String;
        public var groupSourceID:int;
		//public var policy:UploadPolicy;
		public var size:int;
        public var lastDataTime:Date;
		public var feedType:int;
		public var children:ArrayCollection;
		public var role:int;
		//public var definition:AnalysisDefinition;
		public var tagString:String;
		public var attribution:String;
		public var description:String;
		public var ownerName:String;
        public var hasSavedCredentials:Boolean;
        public var solutionTemplate:Boolean;
		
		public function DataFeedDescriptor()
		{
		}

	}
}