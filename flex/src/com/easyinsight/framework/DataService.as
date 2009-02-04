package com.easyinsight.framework
{
	import com.easyinsight.analysis.AnalysisDefinition;
	import com.easyinsight.analysis.FeedMetadata;
	import com.easyinsight.analysis.ListDataResults;
	
	import flash.events.Event;
	import flash.events.EventDispatcher;
	
	import mx.collections.ArrayCollection;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	
	public class DataService extends EventDispatcher implements IDataService
	{
		private var dataRemoteSource:RemoteObject;
		private var _dataFeedID:int;
		private var _availableMeasures:ArrayCollection = new ArrayCollection();
		private var _availableDimensions:ArrayCollection = new ArrayCollection();
		private var onPivotData:Function;
		private var onPivotDataCaller:Object;
		private var onListData:Function;
		private var onListDataCaller:Object;
		private var onMetadata:Function;
		private var onMetadataCaller:Object;
		public var previewMode:Boolean = false;
		
		public function DataService() {
            super();
			dataRemoteSource = new RemoteObject();
			dataRemoteSource.destination = "data";			
			dataRemoteSource.getFeedMetadata.addEventListener(ResultEvent.RESULT, processFeedMetadata);
			dataRemoteSource.getFeedMetadata.addEventListener(FaultEvent.FAULT, GenericFaultHandler.genericFault);			 		
			dataRemoteSource.pivot.addEventListener(ResultEvent.RESULT, processPivotData);
			dataRemoteSource.pivot.addEventListener(FaultEvent.FAULT, GenericFaultHandler.genericFault);
			dataRemoteSource.list.addEventListener(ResultEvent.RESULT, processListData);
			dataRemoteSource.list.addEventListener(FaultEvent.FAULT, GenericFaultHandler.genericFault);
		}
		
		[Bindable]
		public function get dataFeedID():int {
			return _dataFeedID;
		}
		
		public function set dataFeedID(dataFeedID:int):void {
			_dataFeedID = dataFeedID;
		}
		
		public function reloadInitialContent(caller:Object, functionToCall:Function):void {
			this.onMetadata = functionToCall;
			this.onMetadataCaller = caller;
			dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STARTED));
			var call:Object = dataRemoteSource.getFeedMetadata.send(dataFeedID, previewMode);
			call.marker = _dataFeedID;				
		}
		
		public function processFeedMetadata(resultEvent:ResultEvent):void {
			var call:Object = resultEvent.token;
			var feedMetadata:FeedMetadata = dataRemoteSource.getFeedMetadata.lastResult as FeedMetadata;
			onMetadata.call(onMetadataCaller, feedMetadata);
			dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STOPPED));	
		}					
		
		public function get availableMeasures():ArrayCollection {
			return _availableMeasures;		
		}
		
		public function get availableDimensions():ArrayCollection {
			return _availableDimensions;
		}
		
		public function getListData(analysisDefinition:AnalysisDefinition, onListDataCaller:Object,
			functionCall:Function):void {
			onListData = functionCall;
			this.onListDataCaller = onListDataCaller;
			dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STARTED));
			dataRemoteSource.list.send(analysisDefinition, previewMode, null);		
		}
		
		public function processListData(resultEvent:Event):void {			
			var listData:ListDataResults = dataRemoteSource.list.lastResult as ListDataResults;
            if (listData.invalidAnalysisItemIDs != null && listData.invalidAnalysisItemIDs.length > 0) {
                dispatchEvent(new InvalidFieldsEvent(listData.invalidAnalysisItemIDs, listData.feedMetadata));
            }
			onListData.call(onListDataCaller, listData);
			dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STOPPED));
		}
		
		public function getPivotData(analysisDefinition:AnalysisDefinition, onPivotDataCaller:Object, 
			functionCall:Function):void {
			onPivotData = functionCall;
			this.onPivotDataCaller = onPivotDataCaller;
			dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STARTED));
			dataRemoteSource.pivot.send(analysisDefinition, previewMode, null);				
		}
		
		public function processPivotData(resultEvent:Event):void {			
			var pivotData:Object = dataRemoteSource.pivot.lastResult;
			onPivotData.call(onPivotDataCaller, pivotData);
			dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STOPPED));
		}
	}
}