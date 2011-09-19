package com.easyinsight.administration.feed
{
	import com.easyinsight.customupload.UploadPolicy;
	import com.easyinsight.feedassembly.CompositeFeedDefinition;
	
	import mx.collections.ArrayCollection;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.datafeeds.FeedDefinition")]
	public class FeedDefinitionData
	{
		public var feedName:String;
		public var uploadPolicy:UploadPolicy;
		public var dataFeedID:int;
		public var fields:ArrayCollection = new ArrayCollection();
		public var size:int;
		public var dateCreated:Date;
		public var dateUpdated:Date;

		public var tagCloud:ArrayCollection;
		public var ownerName:String;
		public var attribution:String;
		public var description:String;
		public var analysisDefinitionID:int;
		public var tags:ArrayCollection;
		public var dynamicServiceDefinitionID:int;
		public var dataPersisted:Boolean;
		public var publiclyVisible:Boolean;
		public var accountVisible:Boolean;
		public var marketplaceVisible:Boolean;
        public var apiKey:String;
        public var uncheckedAPIEnabled:Boolean;
        public var uncheckedAPIUsingBasicAuth:Boolean;
        public var inheritAccountAPISettings:Boolean;
        public var visible:Boolean = true;
        public var parentSourceID:int;
        public var folders:ArrayCollection = new ArrayCollection();
        public var lastRefreshStart:Date;
        public var marmotScript:String;
		
		public function FeedDefinitionData()
		{
			
		}

        public function createAdminPages():ArrayCollection {
            return new ArrayCollection();
        }

		public function getFeedType():int {
			return DataFeedType.STATIC;
		}
	}
}