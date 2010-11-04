package com.easyinsight.framework
{
	import com.easyinsight.analysis.AnalysisDefinition;
	import com.easyinsight.analysis.FeedMetadata;
	import com.easyinsight.analysis.ListDataResults;
import com.easyinsight.util.ProgressAlert;

import flash.display.DisplayObject;
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
            ProgressAlert.alert(DisplayObject(caller), "Retrieving data source metadata...", null, dataRemoteSource.getFeedMetadata);
			var call:Object = dataRemoteSource.getFeedMetadata.send(dataFeedID);
			call.marker = _dataFeedID;				
		}
		
		public function processFeedMetadata(resultEvent:ResultEvent):void {
			var feedMetadata:FeedMetadata = dataRemoteSource.getFeedMetadata.lastResult as FeedMetadata;
			onMetadata.call(onMetadataCaller, feedMetadata);
            this.onMetadata = null;
            this.onMetadataCaller = null;
			dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STOPPED));	
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
	}
}