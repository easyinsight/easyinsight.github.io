package com.easyinsight.listing
{
	import com.easyinsight.FullScreenPage;
	import com.easyinsight.administration.feed.FeedAdministrationContainer;
	import com.easyinsight.analysis.AnalysisDefinition;
	import com.easyinsight.customupload.UploadPolicy;
	
	import mx.managers.BrowserManager;

	public class FeedAdminAnalyzeSource implements AnalyzeSource
	{
		private var feedID:int;
		private var analysisDefinition:AnalysisDefinition;
		private var feedName:String;
		private var feedTags:String;
		private var uploadPolicy:UploadPolicy;
		private var feedDescription:String;
		private var feedAttribution:String;
		private var feedOwnerName:String;
		private var feedType:int;		
		
		public function FeedAdminAnalyzeSource(feedID:int, analysisDefinition:AnalysisDefinition, feedName:String,
			feedTags:String, uploadPolicy:UploadPolicy, feedDescription:String, feedAttribution:String, feedOwnerName:String, feedType:int) {
			this.feedID = feedID;
			this.analysisDefinition = analysisDefinition;
			this.feedName = feedName;
			this.feedTags = feedTags;
			this.uploadPolicy = uploadPolicy;
			this.feedDescription = feedDescription;
			this.feedAttribution = feedAttribution;
			this.feedOwnerName = feedOwnerName;
			this.feedType = feedType;
		}

		public function createAnalysisPopup():FullScreenPage {
			var feedAdministrationContainer:FeedAdministrationContainer = new FeedAdministrationContainer();
			feedAdministrationContainer.feedID = feedID;
			feedAdministrationContainer.analysisDefinition = analysisDefinition;
			feedAdministrationContainer.feedAdministrationMode(feedName, feedTags, uploadPolicy, feedDescription, feedOwnerName, feedAttribution, feedType);			
			return feedAdministrationContainer;
		}
		
	}
}