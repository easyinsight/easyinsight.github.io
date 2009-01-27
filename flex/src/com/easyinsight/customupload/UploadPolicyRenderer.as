package com.easyinsight.customupload
{
	import com.easyinsight.analysis.AnalysisDefinition;
	import com.easyinsight.listing.DataFeedDescriptor;
	
	import mx.controls.Label;

	public class UploadPolicyRenderer extends Label
	{
		private var feedDescriptor:DataFeedDescriptor;
		
		public function UploadPolicyRenderer()
		{
			super();
		}
		
		override public function set data(object:Object):void {
			if (object is DataFeedDescriptor) {
				feedDescriptor = object as DataFeedDescriptor;
				switch (feedDescriptor.policy.getPolicyType()) {
					case UploadPolicy.COMMERCIAL:
						text = "Commercial";
						break;
					case UploadPolicy.PUBLIC:
						text = "Public";
						break;
					case UploadPolicy.PRIVATE:
						text = "Private";
						break;
				}
			} else if (object is AnalysisDefinition) {
				var analysisDefinition:AnalysisDefinition = object as AnalysisDefinition;
				switch (analysisDefinition.policy) {
					case UploadPolicy.PUBLIC:
						text = "Public";
						break;
					case UploadPolicy.PRIVATE:
						text = "Private";
						break;	
				} 
			}
		}
		
		override public function get data():Object {
			return feedDescriptor;
		}
	}
}