package com.easyinsight.analysis.service {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.CrossTabDataResults;
import com.easyinsight.analysis.DataServiceEvent;
import com.easyinsight.analysis.IReportDataService;
import com.easyinsight.analysis.RequestParams;
import com.easyinsight.analysis.TextDataResults;
import com.easyinsight.framework.DataServiceLoadingEvent;
import com.easyinsight.framework.GenericFaultHandler;
import com.easyinsight.framework.InsightRequestMetadata;

import flash.events.EventDispatcher;

import mx.collections.ArrayCollection;

import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class TextDataService extends EventDispatcher implements IReportDataService {

    private var dataRemoteSource:RemoteObject;

    public function TextDataService() {
        super();
        dataRemoteSource = new RemoteObject();
        dataRemoteSource.destination = "data";
        dataRemoteSource.getTextDataResults.addEventListener(ResultEvent.RESULT, processListData);
        dataRemoteSource.getTextDataResults.addEventListener(FaultEvent.FAULT, GenericFaultHandler.genericFault);
    }

    private var _preserveValues:Boolean = true;

    public function set preserveValues(value:Boolean):void {
        _preserveValues = value;
    }

    private function processListData(event:ResultEvent):void {
        var listData:TextDataResults = dataRemoteSource.getTextDataResults.lastResult as TextDataResults;
        var text:String = listData.text;
        var props:Object = { text: text};
        dispatchEvent(new DataServiceEvent(DataServiceEvent.DATA_RETURNED, new ArrayCollection(), listData.dataSourceInfo, props, listData.auditMessages,
                listData.reportFault, false, 0, 0, listData.suggestions, true, listData.report));
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STOPPED));
    }

    private var report:AnalysisDefinition;

    public function retrieveData(definition:AnalysisDefinition, refreshAllSources:Boolean, requestParams:RequestParams):void {
        this.report = definition;
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STARTED));
        var metadata:InsightRequestMetadata = new InsightRequestMetadata();
        metadata.utcOffset = new Date().getTimezoneOffset();
        dataRemoteSource.getTextDataResults.send(definition, metadata);
    }
}
}