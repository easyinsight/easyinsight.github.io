package com.easyinsight.listing
{
import com.easyinsight.DataAnalysisContainer;
import com.easyinsight.administration.feed.FeedAdministrationContainer;
import com.easyinsight.analysis.AnalysisCloseEvent;
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.framework.DataService;
import com.easyinsight.framework.ModuleAnalyzeSource;
import flash.display.DisplayObject;
import mx.managers.BrowserManager;

	public class AnalysisDefinitionAnalyzeSource extends ModuleAnalyzeSource
	{
		private var analysisDefinition:AnalysisDefinition;
		private var admin:Boolean;
		
		public function AnalysisDefinitionAnalyzeSource(analysisDefinition:AnalysisDefinition, admin:Boolean = false) {
			this.analysisDefinition = analysisDefinition;
			this.admin = admin;
		}

        override protected function getModuleName():String {
            return "/app/easyui-debug/DataAnalysisModule.swf";
        }

        override public function createDirect():DisplayObject {
            var dataAnalysisContainer:DataAnalysisContainer = new DataAnalysisContainer();
            var dataService:DataService = new DataService();
            dataService.dataFeedID = analysisDefinition.dataFeedID;
            dataAnalysisContainer.dataService = dataService;
            BrowserManager.getInstance().setTitle("Easy Insight - " + analysisDefinition.name);
            dataAnalysisContainer.openAnalysis(analysisDefinition);
            return dataAnalysisContainer;
        }

        override protected function applyProperties(val:Object):void {
            super.applyProperties(val);
            val["dataSourceID"] = analysisDefinition.dataFeedID;
            val["headerName"] = analysisDefinition.name;
        }

		/*public function createAnalysisPopup():FullScreenPage {


			var dataService:DataService = new DataService();
			dataService.dataFeedID = analysisDefinition.dataFeedID;

			var dataAnalysisContainer:DataAnalysisContainer = new DataAnalysisContainer();			
			dataAnalysisContainer.dataService = dataService;			
			dataAnalysisContainer.label = analysisDefinition.name;			
			dataAnalysisContainer.openAnalysis(analysisDefinition);
			return dataAnalysisContainer;						

		}*/
		
	}
}