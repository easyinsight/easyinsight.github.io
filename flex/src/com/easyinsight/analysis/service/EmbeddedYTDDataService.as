/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/2/11
 * Time: 9:38 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.service {
import com.easyinsight.analysis.EmbeddedDataServiceEvent;
import com.easyinsight.analysis.IEmbeddedDataService;
import com.easyinsight.analysis.ytd.EmbeddedYTDResults;
import com.easyinsight.framework.DataServiceLoadingEvent;
import com.easyinsight.framework.InsightRequestMetadata;

import flash.events.EventDispatcher;

import mx.collections.ArrayCollection;

import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;

import mx.rpc.remoting.RemoteObject;

public class EmbeddedYTDDataService extends EventDispatcher implements IEmbeddedDataService {

    private var dataRemoteSource:RemoteObject;

    public function EmbeddedYTDDataService() {
        super();
        dataRemoteSource = new RemoteObject();
        dataRemoteSource.destination = "data";
        dataRemoteSource.getEmbeddedYTDResults.addEventListener(ResultEvent.RESULT, processListData);
        dataRemoteSource.getEmbeddedYTDResults.addEventListener(FaultEvent.FAULT, onFault);
    }

    private function onFault(event:FaultEvent):void {
        dispatchEvent(new ReportRetrievalFault(event.fault.message));
    }

    private function processListData(event:ResultEvent):void {
        var listData:EmbeddedYTDResults = dataRemoteSource.getEmbeddedYTDResults.lastResult as EmbeddedYTDResults;

        dispatchEvent(new EmbeddedDataServiceEvent(EmbeddedDataServiceEvent.DATA_RETURNED, listData.dataSet, listData.definition, listData.dataSourceAccessible,
                listData.reportFault, listData.dataSourceInfo, listData.additionalProperties, listData.dataSet.length > 0));
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STOPPED));
    }

    public function retrieveData(reportID:int, dataSourceID:int, filters:ArrayCollection, refreshAll:Boolean, drillthroughFilters:ArrayCollection,
                                 noCache:Boolean, hierarchyOverrides:ArrayCollection):void {
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STARTED));
        var insightRequestMetadata:InsightRequestMetadata = new InsightRequestMetadata();
        insightRequestMetadata.refreshAllSources = refreshAll;
        insightRequestMetadata.noCache = noCache;
        insightRequestMetadata.hierarchyOverrides = hierarchyOverrides;
        dataRemoteSource.getEmbeddedYTDResults.send(reportID, dataSourceID, filters, insightRequestMetadata, drillthroughFilters);
    }

    public function set preserveValues(value:Boolean):void {
    }
}
}
