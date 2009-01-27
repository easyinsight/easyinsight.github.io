package com.easyinsight.administration.feed
{
	import com.easyinsight.analysis.AnalysisItem;
	import com.easyinsight.analysis.AnalysisItemTypes;
	import com.easyinsight.customupload.UploadPolicy;
	
	import mx.collections.ArrayCollection;
	
	[Bindable]
	public class FeedState
	{
		public var feedName:String;
		public var analysisItems:ArrayCollection;		
		public var uploadPolicy:UploadPolicy;
		public var dataFeedID:int;
		public var feedType:int;
		public var size:int;
		public var tagCloud:TagCloud;
		private var feedDefinitionData:FeedDefinitionData;
		
		public function FeedState(feedDefinitionData:FeedDefinitionData) {
			this.feedDefinitionData = feedDefinitionData;
			this.feedName = feedDefinitionData.feedName;
			this.dataFeedID = feedDefinitionData.dataFeedID;
			this.uploadPolicy = feedDefinitionData.uploadPolicy;
			this.analysisItems = feedDefinitionData.fields;
			this.size = feedDefinitionData.size;
			this.tagCloud = feedDefinitionData.tagCloud;
		}
		
		private function isMeasure(dataFeedItem:AnalysisItem, index:int, arr:Array):Boolean {
			return (dataFeedItem.hasType(AnalysisItemTypes.MEASURE));
		}
		
		private function isDimension(dataFeedItem:AnalysisItem, index:int, arr:Array):Boolean {
			return (dataFeedItem.hasType(AnalysisItemTypes.DIMENSION));
		}
		
		public function createFeedDefinitionData():FeedDefinitionData {			
			feedDefinitionData.feedName = feedName;		
			feedDefinitionData.fields = analysisItems;
			feedDefinitionData.uploadPolicy = uploadPolicy;
			feedDefinitionData.dataFeedID = dataFeedID;
			feedDefinitionData.size = size;
			feedDefinitionData.tagCloud = tagCloud;
			return feedDefinitionData;
		}
	}
}