package com.easyinsight.analysis.service {

import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.CrossTabDataResults;
import com.easyinsight.analysis.DataServiceEvent;
import com.easyinsight.analysis.IReportDataService;
import com.easyinsight.framework.DataServiceLoadingEvent;
import com.easyinsight.framework.GenericFaultHandler;
import com.easyinsight.framework.InsightRequestMetadata;
import flash.events.EventDispatcher;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class CrosstabDataService extends EventDispatcher implements IReportDataService {

    private var dataRemoteSource:RemoteObject;

    public function CrosstabDataService() {
        super();
        dataRemoteSource = new RemoteObject();
        dataRemoteSource.destination = "data";
        dataRemoteSource.getCrosstabDataResults.addEventListener(ResultEvent.RESULT, processListData);
        dataRemoteSource.getCrosstabDataResults.addEventListener(FaultEvent.FAULT, GenericFaultHandler.genericFault);
    }

    private var _preserveValues:Boolean = true;

    public function set preserveValues(value:Boolean):void {
        _preserveValues = value;
    }

    private function processListData(event:ResultEvent):void {
        var listData:CrossTabDataResults = dataRemoteSource.getCrosstabDataResults.lastResult as CrossTabDataResults;
        var props:Object = new Object();
        props["columnCount"] = listData.columnCount;
        dispatchEvent(new DataServiceEvent(DataServiceEvent.DATA_RETURNED, listData.dataSet, listData.dataSourceInfo, props, listData.auditMessages,
                listData.reportFault));
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STOPPED));
    }

    private var report:AnalysisDefinition;

    public function retrieveData(definition:AnalysisDefinition, refreshAll:Boolean):void {
        this.report = definition;
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STARTED));
        var metadata:InsightRequestMetadata = new InsightRequestMetadata();
        metadata.utcOffset = new Date().getTimezoneOffset();
        dataRemoteSource.getCrosstabDataResults.send(definition, metadata);
    }
}
}