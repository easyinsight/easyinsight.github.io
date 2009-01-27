package com.easyinsight.framework
{
	import com.easyinsight.analysis.AnalysisDefinition;
	
	public interface IDataService
	{
		import mx.collections.ArrayCollection;
		function get dataFeedID():int;
		function set dataFeedID(dataFeedID:int):void;
		function getPivotData(analysisDefinition:AnalysisDefinition, onPivotDataCaller:Object, functionCall:Function):void;
		function getListData(analysisDefinition:AnalysisDefinition, onListDataCaller:Object, functionCall:Function):void;
		function reloadInitialContent(caller:Object, functionToCall:Function):void;
	}
}