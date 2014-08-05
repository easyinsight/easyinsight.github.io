package com.easyinsight.analysis.service {
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.EmbeddedDataResults;
import com.easyinsight.analysis.EmbeddedDataServiceEvent;
import com.easyinsight.analysis.IEmbeddedDataService;
import com.easyinsight.analysis.RequestParams;
import com.easyinsight.analysis.Value;
import com.easyinsight.framework.DataServiceLoadingEvent;
import com.easyinsight.framework.InsightRequestMetadata;

import flash.events.EventDispatcher;

import mx.collections.ArrayCollection;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class EmbeddedHTMLDataService extends EventDispatcher implements IEmbeddedDataService {

    private var dataRemoteSource:RemoteObject;
    private var _preserveValues:Boolean;

    public function EmbeddedHTMLDataService() {
        super();
        dataRemoteSource = new RemoteObject();
        dataRemoteSource.destination = "data";
        dataRemoteSource.getEmbeddedResults.addEventListener(ResultEvent.RESULT, processListData);
        dataRemoteSource.moreEmbeddedResults.addEventListener(ResultEvent.RESULT, processMoreListData);
        dataRemoteSource.getEmbeddedResults.addEventListener(FaultEvent.FAULT, onFault);
    }

    private function onFault(event:FaultEvent):void {
        dispatchEvent(new ReportRetrievalFault(event.fault.message));
    }

    public function set preserveValues(value:Boolean):void {
        _preserveValues = value;
    }

    protected function translate(listData:EmbeddedDataResults):ArrayCollection {
        var headers:ArrayCollection = new ArrayCollection(listData.headers);
            var data:Array = [];
        if (listData.rows) {
            for (var i:int = 0; i < listData.rows.length; i++) {
                var row:Object = listData.rows[i];
                var values:Array = row.values as Array;
                var endObject:Object = new Object();
                for (var j:int = 0; j < headers.length; j++) {
                    var headerDimension:AnalysisItem = headers[j];
                    var value:Value = values[j];
                    var key:String = headerDimension.qualifiedName();
                    if (_preserveValues) {
                        endObject[key] = value;
                    } else {
                        endObject[key] = value.getValue();
                    }

                    if (value.links != null) {
                        for (var linkKey:String in value.links) {
                            endObject[linkKey + "_link"] = value.links[linkKey];
                        }
                    }
                }
                data.push(endObject);
            }
        }
        return new ArrayCollection(data);
    }

    private function processListData(event:ResultEvent):void {
        var listData:EmbeddedDataResults = dataRemoteSource.getEmbeddedResults.lastResult as EmbeddedDataResults;
        listData.additionalProperties["iframeKey"] = listData.cacheForHTMLKey;
        dispatchEvent(new EmbeddedDataServiceEvent(EmbeddedDataServiceEvent.DATA_RETURNED, translate(listData), listData.definition, listData.dataSourceAccessible,
                listData.reportFault, listData.dataSourceInfo, listData.additionalProperties, listData.rows != null && listData.rows.length > 0));
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STOPPED));
    }

    private function processMoreListData(event:ResultEvent):void {
        var listData:EmbeddedDataResults = dataRemoteSource.moreEmbeddedResults.lastResult as EmbeddedDataResults;
        dispatchEvent(new EmbeddedDataServiceEvent(EmbeddedDataServiceEvent.DATA_RETURNED, translate(listData), listData.definition, listData.dataSourceAccessible,
                listData.reportFault, listData.dataSourceInfo, listData.additionalProperties, listData.rows != null && listData.rows.length > 0));
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STOPPED));
    }

    public function retrieveData(reportID:int, dataSourceID:int, filters:ArrayCollection, refreshAll:Boolean, drillthroughFilters:ArrayCollection, noCache:Boolean, hierarchyOverrides:ArrayCollection, requestParams:RequestParams, additionalAnalysisItems:ArrayCollection):void {
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STARTED));
        var insightRequestMetadata:InsightRequestMetadata = new InsightRequestMetadata();
        insightRequestMetadata.refreshAllSources = refreshAll;
        if (additionalAnalysisItems != null) {
            insightRequestMetadata.additionalAnalysisItems = additionalAnalysisItems;
        }
        insightRequestMetadata.cacheForHTML = true;
        insightRequestMetadata.utcOffset = new Date().getTimezoneOffset();
        insightRequestMetadata.noCache = noCache;
        insightRequestMetadata.hierarchyOverrides = hierarchyOverrides;
        if (requestParams.uid == null) {
            dataRemoteSource.getEmbeddedResults.send(reportID, dataSourceID, filters, insightRequestMetadata, drillthroughFilters);
        } else {
            dataRemoteSource.moreEmbeddedResults.send(reportID, dataSourceID, filters, insightRequestMetadata, drillthroughFilters, requestParams.uid);
        }
    }
}
}