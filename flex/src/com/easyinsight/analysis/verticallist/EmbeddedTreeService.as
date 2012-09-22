/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/14/11
 * Time: 9:01 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.verticallist {
import com.easyinsight.analysis.EmbeddedDataServiceEvent;
import com.easyinsight.analysis.EmbeddedTreeDataResults;
import com.easyinsight.analysis.EmbeddedTrendDataResults;
import com.easyinsight.analysis.IEmbeddedDataService;
import com.easyinsight.analysis.service.EmbeddedDataService;
import com.easyinsight.framework.DataServiceLoadingEvent;
import com.easyinsight.framework.GenericFaultHandler;
import com.easyinsight.framework.InsightRequestMetadata;

import mx.collections.ArrayCollection;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class EmbeddedTreeService extends EmbeddedDataService implements IEmbeddedDataService {

    private var dataRemoteSource:RemoteObject;

    public function EmbeddedTreeService() {
        dataRemoteSource = new RemoteObject();
        dataRemoteSource.destination = "data";
        dataRemoteSource.getEmbeddedTreeResults.addEventListener(ResultEvent.RESULT, processListData);
        dataRemoteSource.getEmbeddedTreeResults.addEventListener(FaultEvent.FAULT, GenericFaultHandler.genericFault);
    }

    override public function retrieveData(reportID:int, dataSourceID:int, filters:ArrayCollection, refreshAll:Boolean, drillthroughFilters:ArrayCollection,
            noCache:Boolean, hierarchyOverrides:ArrayCollection):void {
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STARTED));
        var metadata:InsightRequestMetadata = new InsightRequestMetadata();
        metadata.utcOffset = new Date().getTimezoneOffset();
        dataRemoteSource.getEmbeddedTreeResults.send(reportID, dataSourceID, filters, metadata, drillthroughFilters);
    }

    private function processListData(event:ResultEvent):void {
        var results:EmbeddedTreeDataResults = dataRemoteSource.getEmbeddedTreeResults.lastResult as EmbeddedTreeDataResults;
        var obj:Object = new Object();
        obj["reportEditor"] = false;
        dispatchEvent(new EmbeddedDataServiceEvent(EmbeddedDataServiceEvent.DATA_RETURNED, results.treeRows, results.definition, results.dataSourceAccessible,
                results.reportFault, results.dataSourceInfo, obj, results.treeRows.length > 0));
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STOPPED));
    }
}
}
