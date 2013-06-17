/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/14/11
 * Time: 9:01 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.verticallist {
import com.easyinsight.analysis.EmbeddedCrosstabDataResults;
import com.easyinsight.analysis.EmbeddedDataServiceEvent;
import com.easyinsight.analysis.IEmbeddedDataService;
import com.easyinsight.analysis.RequestParams;
import com.easyinsight.analysis.service.EmbeddedDataService;
import com.easyinsight.framework.DataServiceLoadingEvent;
import com.easyinsight.framework.GenericFaultHandler;
import com.easyinsight.framework.InsightRequestMetadata;

import mx.collections.ArrayCollection;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class EmbeddedCrosstabService extends EmbeddedDataService implements IEmbeddedDataService {

    private var dataRemoteSource:RemoteObject;

    public function EmbeddedCrosstabService() {
        dataRemoteSource = new RemoteObject();
        dataRemoteSource.destination = "data";
        dataRemoteSource.getEmbeddedCrosstabResults.addEventListener(ResultEvent.RESULT, processListData);
        dataRemoteSource.getEmbeddedCrosstabResults.addEventListener(FaultEvent.FAULT, GenericFaultHandler.genericFault);
    }

    override public function retrieveData(reportID:int, dataSourceID:int, filters:ArrayCollection, refreshAll:Boolean, drillthroughFilters:ArrayCollection, noCache:Boolean, hierarchyOverrides:ArrayCollection, requestParams:RequestParams, additionalAnalysisItems:ArrayCollection):void {
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STARTED));
        var metadata:InsightRequestMetadata = new InsightRequestMetadata();
        metadata.utcOffset = new Date().getTimezoneOffset();
        metadata.hierarchyOverrides = hierarchyOverrides;
        dataRemoteSource.getEmbeddedCrosstabResults.send(reportID, dataSourceID, filters, metadata, drillthroughFilters);
    }

    private function processListData(event:ResultEvent):void {
        var results:EmbeddedCrosstabDataResults = dataRemoteSource.getEmbeddedCrosstabResults.lastResult as EmbeddedCrosstabDataResults;
        var props:Object = new Object();
        props["columnCount"] = results.columnCount;
        dispatchEvent(new EmbeddedDataServiceEvent(EmbeddedDataServiceEvent.DATA_RETURNED, results.dataSet, results.definition, results.dataSourceAccessible,
                results.reportFault, results.dataSourceInfo, props, results.dataSet.length > 3));
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STOPPED));
    }
}
}
