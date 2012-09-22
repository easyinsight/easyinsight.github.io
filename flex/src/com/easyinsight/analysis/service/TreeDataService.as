package com.easyinsight.analysis.service {

import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.CrossTabDataResults;
import com.easyinsight.analysis.DataServiceEvent;
import com.easyinsight.analysis.IReportDataService;
import com.easyinsight.analysis.TreeDataResults;
import com.easyinsight.framework.DataServiceLoadingEvent;
import com.easyinsight.framework.GenericFaultHandler;
import com.easyinsight.framework.InsightRequestMetadata;

import flash.events.EventDispatcher;

import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class TreeDataService extends EventDispatcher implements IReportDataService {

    private var dataRemoteSource:RemoteObject;

    public function TreeDataService() {
        super();
        dataRemoteSource = new RemoteObject();
        dataRemoteSource.destination = "data";
        dataRemoteSource.getTreeDataResults.addEventListener(ResultEvent.RESULT, processListData);
        dataRemoteSource.getTreeDataResults.addEventListener(FaultEvent.FAULT, GenericFaultHandler.genericFault);
    }

    private var _preserveValues:Boolean = true;

    public function set preserveValues(value:Boolean):void {
        _preserveValues = value;
    }

    private function processListData(event:ResultEvent):void {
        var listData:TreeDataResults = dataRemoteSource.getTreeDataResults.lastResult as TreeDataResults;
        var props:Object = new Object();
        dispatchEvent(new DataServiceEvent(DataServiceEvent.DATA_RETURNED, listData.treeRows, listData.dataSourceInfo, props, listData.auditMessages,
                listData.reportFault, false, 0, 0, listData.suggestions, listData.treeRows != null && listData.treeRows.length > 0));
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STOPPED));
    }

    private var report:AnalysisDefinition;

    public function retrieveData(definition:AnalysisDefinition, refreshAll:Boolean):void {
        this.report = definition;
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STARTED));
        var metadata:InsightRequestMetadata = new InsightRequestMetadata();
        metadata.utcOffset = new Date().getTimezoneOffset();
        dataRemoteSource.getTreeDataResults.send(definition, metadata);
    }
}
}