package com.easyinsight.framework
{
	import com.easyinsight.analysis.AnalysisDefinition;
	
	public interface IDataService
	{
		function get dataFeedID():int;
		function set dataFeedID(dataFeedID:int):void;
		function getListData(analysisDefinition:AnalysisDefinition, onListDataCaller:Object, functionCall:Function):void;
		function reloadInitialContent(caller:Object, functionToCall:Function):void;
	}
}