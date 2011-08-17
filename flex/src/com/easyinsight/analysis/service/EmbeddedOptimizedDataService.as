package com.easyinsight.analysis.service {

import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.EmbeddedDataResults;
import com.easyinsight.analysis.EmbeddedDataServiceEvent;
import com.easyinsight.analysis.IEmbeddedDataService;
import com.easyinsight.analysis.Value;
import com.easyinsight.dashboard.OptimizedDataServiceEvent;
import com.easyinsight.framework.DataServiceLoadingEvent;
import com.easyinsight.framework.InsightRequestMetadata;

import flash.events.EventDispatcher;
import mx.collections.ArrayCollection;
import mx.controls.Alert;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class EmbeddedOptimizedDataService extends EventDispatcher implements IEmbeddedDataService {

    private var dataRemoteSource:RemoteObject;
    private var _preserveValues:Boolean;

    public function EmbeddedOptimizedDataService() {
        super();
        dataRemoteSource = new RemoteObject();
        dataRemoteSource.destination = "data";
        dataRemoteSource.getOptimizedResults.addEventListener(ResultEvent.RESULT, processListData);
        dataRemoteSource.getOptimizedResults.addEventListener(FaultEvent.FAULT, onFault);
    }

    private function onFault(event:FaultEvent):void {
        dispatchEvent(new ReportRetrievalFault(event.fault.message));
    }

    public function set preserveValues(value:Boolean):void {
        _preserveValues = value;
    }

    protected function translate(listData:EmbeddedDataResults, report:AnalysisDefinition):ArrayCollection {
        var headers:ArrayCollection = new ArrayCollection(listData.headers);
            var rows:ArrayCollection = new ArrayCollection(listData.rows);
            var data:ArrayCollection = new ArrayCollection();
            for (var i:int = 0; i < rows.length; i++) {
                var row:Object = rows.getItemAt(i);
                var values:Array = row.values as Array;
                var endObject:Object = new Object();
                for (var j:int = 0; j < headers.length; j++) {
                    var headerDimension:AnalysisItem = headers[j];
                    var value:Value = values[j];
                    var key:String = headerDimension.qualifiedName();
                    /*if (_preserveValues) {
                        endObject[key] = value;
                    } else {*/
                        endObject[key] = value.getValue();
                    //}
                }
                data.addItem(endObject);
            }
        return data;
    }

    private function processListData(event:ResultEvent):void {
        var results:Object = dataRemoteSource.getOptimizedResults.lastResult;
        var eventMap:Object = new Object();
        for (var reportID:String in results) {
            var embeddedDataResults:EmbeddedDataResults = results[reportID] as EmbeddedDataResults;
            eventMap[reportID] = new EmbeddedDataServiceEvent(EmbeddedDataServiceEvent.DATA_RETURNED, translate(embeddedDataResults, embeddedDataResults.definition), embeddedDataResults.definition, embeddedDataResults.dataSourceAccessible,
                embeddedDataResults.reportFault, embeddedDataResults.dataSourceInfo, embeddedDataResults.additionalProperties)
        }
        dispatchEvent(new OptimizedDataServiceEvent(eventMap));
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STOPPED));
    }

    public function retrieveReportData(reportIDs:ArrayCollection, dataSourceID:int, filters:ArrayCollection):void {
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STARTED));
        var insightRequestMetadata:InsightRequestMetadata = new InsightRequestMetadata();
        insightRequestMetadata.utcOffset = new Date().getTimezoneOffset();
        dataRemoteSource.getOptimizedResults.send(reportIDs, dataSourceID, filters, insightRequestMetadata);
    }

    public function retrieveData(reportID:int, dataSourceID:int, filters:ArrayCollection, refreshAll:Boolean, drillthroughFilters:ArrayCollection, noCache:Boolean, hierarchyOverrides:ArrayCollection):void {
    }
}
}