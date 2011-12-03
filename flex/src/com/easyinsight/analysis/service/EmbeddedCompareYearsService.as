/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/3/11
 * Time: 11:17 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.service {
import com.easyinsight.analysis.EmbeddedDataServiceEvent;
import com.easyinsight.analysis.IEmbeddedDataService;
import com.easyinsight.analysis.ytd.EmbeddedCompareYearsResult;
import com.easyinsight.framework.DataServiceLoadingEvent;
import com.easyinsight.framework.InsightRequestMetadata;

import flash.events.EventDispatcher;

import mx.collections.ArrayCollection;

import mx.rpc.events.FaultEvent;

import mx.rpc.events.ResultEvent;

import mx.rpc.remoting.RemoteObject;

public class EmbeddedCompareYearsService extends EventDispatcher implements IEmbeddedDataService {

    private var dataRemoteSource:RemoteObject;

    public function EmbeddedCompareYearsService() {
        super();
        dataRemoteSource = new RemoteObject();
        dataRemoteSource.destination = "data";
        dataRemoteSource.getEmbeddedCompareYearsResults.addEventListener(ResultEvent.RESULT, processListData);
        dataRemoteSource.getEmbeddedCompareYearsResults.addEventListener(FaultEvent.FAULT, onFault);
    }

    private function onFault(event:FaultEvent):void {
        dispatchEvent(new ReportRetrievalFault(event.fault.message));
    }

    private function processListData(event:ResultEvent):void {
        var listData:EmbeddedCompareYearsResult = dataRemoteSource.getEmbeddedCompareYearsResults.lastResult as EmbeddedCompareYearsResult;

        dispatchEvent(new EmbeddedDataServiceEvent(EmbeddedDataServiceEvent.DATA_RETURNED, listData.dataSet, listData.definition, listData.dataSourceAccessible,
                listData.reportFault, listData.dataSourceInfo, listData.additionalProperties));
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STOPPED));
    }

    public function retrieveData(reportID:int, dataSourceID:int, filters:ArrayCollection, refreshAll:Boolean, drillthroughFilters:ArrayCollection,
                                 noCache:Boolean, hierarchyOverrides:ArrayCollection):void {
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STARTED));
        var insightRequestMetadata:InsightRequestMetadata = new InsightRequestMetadata();
        insightRequestMetadata.refreshAllSources = refreshAll;
        insightRequestMetadata.noCache = noCache;
        insightRequestMetadata.hierarchyOverrides = hierarchyOverrides;
        dataRemoteSource.getEmbeddedCompareYearsResults.send(reportID, dataSourceID, filters, insightRequestMetadata, drillthroughFilters);
    }

    public function set preserveValues(value:Boolean):void {
    }
}
}
