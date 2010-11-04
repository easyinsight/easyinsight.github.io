package com.easyinsight.analysis.service {

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.EmbeddedDataResults;
import com.easyinsight.analysis.EmbeddedDataServiceEvent;
import com.easyinsight.analysis.IEmbeddedDataService;
import com.easyinsight.analysis.Value;
import com.easyinsight.framework.DataServiceLoadingEvent;
import com.easyinsight.framework.InsightRequestMetadata;

import flash.events.EventDispatcher;
import mx.collections.ArrayCollection;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class EmbeddedDataService extends EventDispatcher implements IEmbeddedDataService {

    private var dataRemoteSource:RemoteObject;
    private var _preserveValues:Boolean;

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

    public function set preserveValues(value:Boolean):void {
        _preserveValues = value;
    }

    private function processListData(event:ResultEvent):void {
        var listData:EmbeddedDataResults = dataRemoteSource.getEmbeddedResults.lastResult as EmbeddedDataResults;
        if (listData.reportFault == null) {
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
                data.addItem(endObject);
            }
        }
        dispatchEvent(new EmbeddedDataServiceEvent(EmbeddedDataServiceEvent.DATA_RETURNED, data, listData.definition, listData.dataSourceAccessible,
                listData.attribution, listData.reportFault, listData.dataSourceInfo, listData.ratingsAverage,
                listData.ratingsCount, listData.myRating, listData.additionalProperties));
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STOPPED));
    }

    public function retrieveData(reportID:int, dataSourceID:int, filters:ArrayCollection, refreshAll:Boolean, drillthroughFilters:ArrayCollection,
            noCache:Boolean, hierarchyOverrides:ArrayCollection):void {
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STARTED));
        var insightRequestMetadata:InsightRequestMetadata = new InsightRequestMetadata();
        insightRequestMetadata.refreshAllSources = refreshAll;
        insightRequestMetadata.utcOffset = new Date().getTimezoneOffset();
        insightRequestMetadata.noCache = noCache;
        insightRequestMetadata.hierarchyOverrides = hierarchyOverrides;
        dataRemoteSource.getEmbeddedResults.send(reportID, dataSourceID, filters, insightRequestMetadata, drillthroughFilters);
    }
}
}