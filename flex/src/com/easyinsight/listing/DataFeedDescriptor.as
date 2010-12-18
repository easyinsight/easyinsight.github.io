package com.easyinsight.listing
{
	import com.easyinsight.analysis.AnalysisDefinition;
	import com.easyinsight.customupload.UploadPolicy;

import com.easyinsight.solutions.DataSourceDescriptor;

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

        public static const GOOGLE_ANALYTICS:int = 13;
        public static const BASECAMP:int = 18;
        public static const CLOUD_WATCH:int = 21;
        public static const CLARITY:int = 22;
        public static const HIGHRISE:int = 23;
        public static const TWITTER:int = 26;
        public static const MARKETO:int = 31;
        public static const PIVOTAL_TRACKER:int = 34;
        public static const SENDGRID:int = 35;
        public static const MEETUP:int = 38;
        public static const LINKEDIN:int = 39;
        public static const FRESHBOOKS:int = 49;
        public static const REDIRECT:int = 54;
        public static const WHOLE_FOODS:int = 56;
        public static const CONSTANT_CONTACT:int = 63;
        public static const QUICKBASE:int = 72;

		public static const OWNER:int = 1;
		public static const SUBSCRIBER:int = 3;
		
		public var name:String;
		public var id:int;
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

        public function toDataSourceDescriptor():DataSourceDescriptor {
            var dsd:DataSourceDescriptor = new DataSourceDescriptor();
            dsd.id = id;
            dsd.name = name;
            dsd.dataSourceType = feedType;
            return dsd;
        }


        }
}