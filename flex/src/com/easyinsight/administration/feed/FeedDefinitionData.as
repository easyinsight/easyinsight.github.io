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
		public var genre:String;
		public var uploadPolicy:UploadPolicy;
		public var dataFeedID:int;
		public var fields:ArrayCollection;
		public var size:int;
		public var dateCreated:Date;
		public var dateUpdated:Date;
		public var viewCount:int;
		public var ratingCount:int;
		public var ratingAverage:Number = 0;
		public var tagCloud:ArrayCollection;
		public var ownerName:String;
		public var attribution:String;
		public var description:String;
		public var analysisDefinitionID:int;
		public var tags:ArrayCollection;
		public var dynamicServiceDefinitionID:int;
		public var ratingSource:String;
		public var dataPersisted:Boolean;
		public var publiclyVisible:Boolean;
		public var marketplaceVisible:Boolean;
        public var apiKey:String;
        public var uncheckedAPIEnabled:Boolean;
        public var uncheckedAPIUsingBasicAuth:Boolean;
        public var validatedAPIEnabled:Boolean;
        public var validatedAPIUsingBasicAuth:Boolean;
        public var inheritAccountAPISettings:Boolean;
        public var refreshDataInterval:int;
        public var virtualDimensions:ArrayCollection;
		
		public function FeedDefinitionData()
		{
			
		}

		public function getFeedType():int {
			return DataFeedType.STATIC;
		}
		
		public static function hack():void {
			var googleDef:GoogleFeedDefinition;
			var fileDef:FileBasedFeedDefinition;
			var compositeDef:CompositeFeedDefinition;
			var adminDef:AdminStatsDataSource;
		}

        public function canRefresh():Boolean {
            return false;
        }
	}
}