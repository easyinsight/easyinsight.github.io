package com.easyinsight.listing
{
	import com.easyinsight.DataAnalysisContainer;
	import com.easyinsight.FullScreenPage;
	import com.easyinsight.analysis.AnalysisDefinition;
	import com.easyinsight.framework.DataService;

	public class AnalysisDefinitionAnalyzeSource implements AnalyzeSource	
	{
		private var analysisDefinition:AnalysisDefinition;
		private var admin:Boolean;
		
		public function AnalysisDefinitionAnalyzeSource(analysisDefinition:AnalysisDefinition, admin:Boolean = false) {
			this.analysisDefinition = analysisDefinition;
			this.admin = admin;
		}

		public function createAnalysisPopup():FullScreenPage {
			
			
			var dataService:DataService = new DataService();
			dataService.dataFeedID = analysisDefinition.dataFeedID;

			var dataAnalysisContainer:DataAnalysisContainer = new DataAnalysisContainer();													
			/*var dataAnalysisContainer:DataAnalysisContainer = DataAnalysisContainer(PopUpManager.createPopUp(parent, 
				DataAnalysisContainer, true));*/
			dataAnalysisContainer.dataService = dataService;			
			dataAnalysisContainer.label = analysisDefinition.name;			
			dataAnalysisContainer.openAnalysis(analysisDefinition);
			/*BrowserManager.getInstance().setTitle("Easy Insight - " + analysisDefinition.name);
			var fragmentObject:Object = new Object();
            fragmentObject.analysisID = analysisDefinition.analysisID;
            var fragmentString:String = URLUtil.objectToString(fragmentObject);
			BrowserManager.getInstance().setFragment(fragmentString);*/
			return dataAnalysisContainer;						
				
		}
		
	}
}