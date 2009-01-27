package com.easyinsight.analysis.options
{
	import com.easyinsight.analysis.AnalysisList;
	
	public class ListOption extends AnalysisItemOption
	{
		public function ListOption()
		{
		}

		override protected function getLabelValue():String {
			return "List";
		}
		
		override public function createAnalysisItem(key:String):AnalysisItem {
			var analysisList:AnalysisList = new AnalysisList();
			analysisList.key = key;
			analysisList.expanded = false;
			analysisList.delimiter = ",";
			return analysisDimension; 
		}
	}
}