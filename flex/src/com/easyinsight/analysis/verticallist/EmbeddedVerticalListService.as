/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/14/11
 * Time: 9:01 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.verticallist {
import com.easyinsight.analysis.EmbeddedDataResults;
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

public class EmbeddedVerticalListService extends EmbeddedDataService implements IEmbeddedDataService {

    private var dataRemoteSource:RemoteObject;

    public function EmbeddedVerticalListService() {
        dataRemoteSource = new RemoteObject();
        dataRemoteSource.destination = "data";
        dataRemoteSource.getEmbeddedVerticalDataResults.addEventListener(ResultEvent.RESULT, processListData);
        dataRemoteSource.getEmbeddedVerticalDataResults.addEventListener(FaultEvent.FAULT, GenericFaultHandler.genericFault);
    }

    override public function retrieveData(reportID:int, dataSourceID:int, filters:ArrayCollection, refreshAll:Boolean, drillthroughFilters:ArrayCollection, noCache:Boolean, hierarchyOverrides:ArrayCollection, requestParams:RequestParams):void {
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STARTED));
        var metadata:InsightRequestMetadata = new InsightRequestMetadata();
        metadata.utcOffset = new Date().getTimezoneOffset();
        dataRemoteSource.getEmbeddedVerticalDataResults.send(reportID, dataSourceID, filters, metadata);
    }

    private function processListData(event:ResultEvent):void {
        var verticalResults:EmbeddedVerticalDataResults = dataRemoteSource.getEmbeddedVerticalDataResults.lastResult as EmbeddedVerticalDataResults;
        var results:ArrayCollection = new ArrayCollection();
        for (var i:int = 0; i < verticalResults.list.length; i++) {
            var dataResults:EmbeddedDataResults = verticalResults.list.getItemAt(i) as EmbeddedDataResults;
            results.addItem(new EmbeddedDataWrapper(translate(dataResults), dataResults.definition));
        }
        var listData:EmbeddedDataResults = verticalResults.list.getItemAt(0) as EmbeddedDataResults;
        dispatchEvent(new EmbeddedDataServiceEvent(EmbeddedDataServiceEvent.DATA_RETURNED, results, verticalResults.report, listData.dataSourceAccessible,
                listData.reportFault, listData.dataSourceInfo, verticalResults.additionalProperties, results.length > 0));
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STOPPED));
    }
}
}
