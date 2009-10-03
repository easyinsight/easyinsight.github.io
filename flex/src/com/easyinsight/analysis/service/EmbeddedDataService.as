package com.easyinsight.analysis.service {

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.EmbeddedDataResults;
import com.easyinsight.analysis.EmbeddedDataServiceEvent;
import com.easyinsight.analysis.IEmbeddedDataService;
import com.easyinsight.analysis.Value;
import com.easyinsight.analysis.conditions.ConditionRenderer;
import com.easyinsight.framework.CredentialsCache;
import com.easyinsight.framework.DataServiceLoadingEvent;
import com.easyinsight.framework.InsightRequestMetadata;

import flash.events.EventDispatcher;
import mx.collections.ArrayCollection;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;
import mx.controls.Alert;

public class EmbeddedDataService extends EventDispatcher implements IEmbeddedDataService {

    private var dataRemoteSource:RemoteObject;

    public function EmbeddedDataService() {
        super();
        dataRemoteSource = new RemoteObject();
        dataRemoteSource.destination = "data";
        dataRemoteSource.getEmbeddedResults.addEventListener(ResultEvent.RESULT, processListData);
        dataRemoteSource.getEmbeddedResults.addEventListener(FaultEvent.FAULT, onFault);
    }

    private function onFault(event:FaultEvent):void {
        dispatchEvent(new ReportRetrievalFault(event.fault.message));
    }

    private function processListData(event:ResultEvent):void {
        var listData:EmbeddedDataResults = dataRemoteSource.getEmbeddedResults.lastResult as EmbeddedDataResults;
        if (listData.credentialRequirements == null || listData.credentialRequirements.length == 0) {
            var clientProcessorMap:Object = new Object();
            var headers:ArrayCollection = new ArrayCollection(listData.headers);
            for each (var analysisItem:AnalysisItem in headers) {
                clientProcessorMap[analysisItem.qualifiedName()] = analysisItem.createClientRenderer();
            }
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
                    endObject[key] = value.getValue();
                    var conditionRenderer:ConditionRenderer = clientProcessorMap[key];
                    conditionRenderer.addValue(value);
                    if (value.links != null) {
                        for (var linkKey:String in value.links) {
                            endObject[linkKey + "_link"] = value.links[linkKey];
                        }
                    }
                }
                data.addItem(endObject);
            }
        }
        dispatchEvent(new EmbeddedDataServiceEvent(EmbeddedDataServiceEvent.DATA_RETURNED, data, listData.definition, clientProcessorMap, listData.dataSourceAccessible,
                listData.attribution, listData.credentialRequirements, listData.dataSourceInfo, listData.ratingsAverage,
                listData.ratingsCount));
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STOPPED));
    }

    public function retrieveData(reportID:int, dataSourceID:int, filters:ArrayCollection, refreshAll:Boolean, drillthroughFilters:ArrayCollection):void {
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STARTED));
        var insightRequestMetadata:InsightRequestMetadata = new InsightRequestMetadata();
        insightRequestMetadata.refreshAllSources = refreshAll;
        insightRequestMetadata.credentialFulfillmentList = CredentialsCache.getCache().createCredentials();
        dataRemoteSource.getEmbeddedResults.send(reportID, dataSourceID, filters, insightRequestMetadata, drillthroughFilters);
    }
}
}