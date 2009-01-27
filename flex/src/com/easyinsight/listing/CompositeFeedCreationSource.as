package com.easyinsight.listing
{
	import com.easyinsight.FullScreenPage;
    import com.easyinsight.feedassembly.NewCompositeFeedWorkspace;
	
	import mx.collections.ArrayCollection;

	public class CompositeFeedCreationSource implements AnalyzeSource
	{
		private var descriptors:ArrayCollection;
		
		public function CompositeFeedCreationSource(descriptors:ArrayCollection)
		{
			this.descriptors = descriptors;
		}

		public function createAnalysisPopup():FullScreenPage
		{
			var compositeFeedWindow:NewCompositeFeedWorkspace = new NewCompositeFeedWorkspace();
			compositeFeedWindow.addFeeds(descriptors);
			return compositeFeedWindow;
		}
		
	}
}