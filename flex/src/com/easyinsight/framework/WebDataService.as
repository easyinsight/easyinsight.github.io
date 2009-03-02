package com.easyinsight.framework
{
	import com.easyinsight.analysis.AnalysisDefinition;
	import com.easyinsight.analysis.AnalysisItem;
	import com.easyinsight.analysis.DateValue;
	import com.easyinsight.analysis.ListDataResults;
	import com.easyinsight.analysis.ListRow;
	import com.easyinsight.analysis.NamedKey;
	import com.easyinsight.analysis.NumericValue;
	import com.easyinsight.analysis.StringValue;
	import com.easyinsight.analysis.Value;
	
	import flash.events.EventDispatcher;
	
	import mx.collections.ArrayCollection;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.soap.WebService;

	public class WebDataService extends EventDispatcher implements IDataService
	{
		private var analysisID:int;
		private var webService:WebService;
		private var onPivotData:Function;
		private var onPivotDataCaller:Object;
		private var onListData:Function;
		private var onListDataCaller:Object;
		private var onChartData:Function;
		private var onChartDataCaller:Object;
		
		public function WebDataService(analysisID:int)
		{
			this.analysisID = analysisID;
			webService = new WebService();
			webService.wsdl = "http://ec2-72-44-53-122.compute-1.amazonaws.com:8080/app/services/InsightAPIService?wsdl";
			webService.list.addEventListener(ResultEvent.RESULT, processListData);
			webService.loadWSDL();
		}

		public function get dataFeedID():int
		{
			return 0;
		}
		
		public function set dataFeedID(dataFeedID:int):void
		{
		}
		
		public function getPivotData(analysisDefinition:AnalysisDefinition, onPivotDataCaller:Object, functionCall:Function):void
		{
		}
		
		public function getListData(analysisDefinition:AnalysisDefinition, onListDataCaller:Object, functionCall:Function):void {
			this.onListData = functionCall;
			this.onListDataCaller = onListDataCaller;
			dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STARTED));
			webService.list.send(null, null, this.analysisID);
		}
		
		private function processListData(event:ResultEvent):void {
			var listDataResults:ListDataResults = new ListDataResults();
			var object:Object = webService.list.lastResult;
			var listItems:Array = [];
			var analysisItems:ArrayCollection = object.analysisItems;
			for each (var analysisItemObj:Object in analysisItems) {
				var keyName:String = analysisItemObj.keyName;
				var analysisItem:AnalysisItem = new AnalysisItem();
				var namedKey:NamedKey = new NamedKey();
				analysisItem.key = namedKey;
				namedKey.name = analysisItemObj.keyName;
				listItems.push(analysisItem);
				trace(keyName);	
			}
			listDataResults.headers = listItems;
			var listRows:ArrayCollection = object.listRows;
			var finalRows:Array = [];
			for each (var rowValues:ArrayCollection in listRows) {
				var listRow:ListRow = new ListRow();
				var listRowValues:Array = [];
				for each (var cell:Object in rowValues) {
					var valueType:int = cell.type;
					var value:Value;
					if (valueType == 1) {
						var stringValue:StringValue = new StringValue();
						stringValue.value = cell.stringValue;
						value = stringValue;
					} else if (valueType == 2) {
						var numericValue:NumericValue = new NumericValue();
						numericValue.value = cell.doubleValue;
						value = numericValue;						
					} else if (valueType == 3) {
						var dateValue:DateValue = new DateValue();
						dateValue.date = cell.dateValue;
						value = dateValue;
					}
					listRowValues.push(value);
					/*trace(valueType);
					var doubleValue:Number = cell.doubleValue;
					trace(doubleValue);
					var stringValue:String = cell.stringValue;
					trace(stringValue);
					var dateValue:String = cell.dateValue;
					trace(dateValue);*/
				}
				listRow.values = listRowValues;
				finalRows.push(listRow);	
			}
			listDataResults.rows = finalRows;
			onListData.call(onListDataCaller, listDataResults);
			dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STOPPED));
		}
		
		public function getChartData(analysisDefinition:AnalysisDefinition, onChartDataCaller:Object, functionCall:Function):void
		{
		}
		
		public function reloadInitialContent(caller:Object, functionToCall:Function):void
		{
		}
		
	}
}