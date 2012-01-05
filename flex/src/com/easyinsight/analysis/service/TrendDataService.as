package com.easyinsight.analysis.service {

import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.DataServiceEvent;
import com.easyinsight.analysis.IReportDataService;
import com.easyinsight.analysis.TrendDataResults;
import com.easyinsight.framework.DataServiceLoadingEvent;
import com.easyinsight.framework.GenericFaultHandler;
import com.easyinsight.framework.InsightRequestMetadata;
import flash.events.EventDispatcher;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class TrendDataService extends EventDispatcher implements IReportDataService {

    private var dataRemoteSource:RemoteObject;

    public function TrendDataService() {
        super();
        dataRemoteSource = new RemoteObject();
        dataRemoteSource.destination = "data";
        dataRemoteSource.getTrendDataResults.addEventListener(ResultEvent.RESULT, processListData);
        dataRemoteSource.getTrendDataResults.addEventListener(FaultEvent.FAULT, GenericFaultHandler.genericFault);
    }


    public function set preserveValues(value:Boolean):void {
    }

    private function processListData(event:ResultEvent):void {
        var listData:TrendDataResults = dataRemoteSource.getTrendDataResults.lastResult as TrendDataResults;
        var obj:Object = new Object();
        obj["reportEditor"] = true;
        dispatchEvent(new DataServiceEvent(DataServiceEvent.DATA_RETURNED, listData.trendOutcomes, listData.dataSourceInfo, obj, listData.auditMessages,
                listData.reportFault, false, 0, 0, listData.suggestions, listData.trendOutcomes != null && listData.trendOutcomes.length > 0));
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STOPPED));
    }

    public function retrieveData(definition:AnalysisDefinition, refreshAll:Boolean):void {
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STARTED));
        var metadata:InsightRequestMetadata = new InsightRequestMetadata();
        metadata.utcOffset = new Date().getTimezoneOffset();
        dataRemoteSource.getTrendDataResults.send(definition, metadata);
    }
}
}