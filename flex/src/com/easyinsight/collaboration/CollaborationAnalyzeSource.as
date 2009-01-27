package com.easyinsight.collaboration
{
	import com.easyinsight.DataAnalysisContainer;
	import com.easyinsight.analysis.AnalysisDefinition;
	import com.easyinsight.framework.DataService;
	import com.easyinsight.listing.AnalyzeSource;
	
	import flash.display.DisplayObject;
	
	import flexlib.mdi.containers.MDICanvas;

	public class CollaborationAnalyzeSource implements AnalyzeSource
	{
		private var analysisDefinition:AnalysisDefinition;
		private var sessionName:String;
		
		public function CollaborationAnalyzeSource(analysisDefinition:AnalysisDefinition, sessionName:String) {
			this.analysisDefinition = analysisDefinition;
			this.sessionName = sessionName;
		}

		public function createAnalysisPopup(parent:DisplayObject):void {
			var canvas:MDICanvas = parent as MDICanvas;
			
			var dataService:DataService = new DataService();
			dataService.dataFeedID = analysisDefinition.dataFeedID;

			var dataAnalysisContainer:DataAnalysisContainer = new DataAnalysisContainer();													
			/*var dataAnalysisContainer:DataAnalysisContainer = DataAnalysisContainer(PopUpManager.createPopUp(parent, 
				DataAnalysisContainer, true));*/
			dataAnalysisContainer.dataService = dataService;
			dataAnalysisContainer.label = analysisDefinition.name;
			dataAnalysisContainer.openAnalysis(analysisDefinition);
			canvas.windowManager.add(dataAnalysisContainer);
			dataAnalysisContainer.joinCollaboration(sessionName);	
		}
		
	}
}