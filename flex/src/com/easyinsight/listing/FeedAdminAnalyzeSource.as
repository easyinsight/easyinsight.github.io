package com.easyinsight.listing
{
import com.easyinsight.administration.feed.FeedAdministrationContainer;
import com.easyinsight.customupload.UploadPolicy;
	
    import com.easyinsight.framework.ModuleAnalyzeSource;
import flash.display.DisplayObject;

	public class FeedAdminAnalyzeSource extends ModuleAnalyzeSource
	{
		private var feedID:int;
		private var feedName:String;
		private var feedTags:String;
		private var uploadPolicy:UploadPolicy;
		private var feedDescription:String;
		private var feedAttribution:String;
		private var feedOwnerName:String;
		private var feedType:int;		
		
		public function FeedAdminAnalyzeSource(feedID:int, feedName:String,
			feedTags:String, uploadPolicy:UploadPolicy, feedDescription:String, feedAttribution:String, feedOwnerName:String, feedType:int) {
			this.feedID = feedID;
			this.feedName = feedName;
			this.feedTags = feedTags;
			this.uploadPolicy = uploadPolicy;
			this.feedDescription = feedDescription;
			this.feedAttribution = feedAttribution;
			this.feedOwnerName = feedOwnerName;
			this.feedType = feedType;
		}

        override public function createDirect():DisplayObject {
            var feedAdminContainer:FeedAdministrationContainer = new FeedAdministrationContainer();
            feedAdminContainer.feedID = feedID;
            feedAdminContainer.feedAdministrationMode(feedName, feedTags, uploadPolicy, feedDescription, feedOwnerName, feedAttribution, feedType);
            return feedAdminContainer;
        }

        override protected function getModuleName():String {
            return "/app/easyui-debug/DataSourceAdminModule.swf";
        }

        override protected function applyProperties(val:Object):void {
            super.applyProperties(val);
            val["feedName"] = feedName;
            val["feedID"] = feedID;
            val["feedTags"] = feedTags;
            val["uploadPolicy"] = uploadPolicy;
            val["feedDescription"] = feedDescription;
            val["feedOwnerName"] = feedOwnerName;
            val["feedAttribution"] = feedAttribution;
            val["feedType"] = feedType;
        }

	}
}