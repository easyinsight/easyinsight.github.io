package com.easyinsight.analysis.timeline {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.DataServiceEvent;
import com.easyinsight.analysis.IReportDataService;
import com.easyinsight.analysis.ListDataResults;
import com.easyinsight.analysis.SeriesDataResults;
import com.easyinsight.analysis.service.ListDataService;
import com.easyinsight.framework.DataServiceLoadingEvent;
import com.easyinsight.framework.GenericFaultHandler;
import com.easyinsight.framework.InsightRequestMetadata;

import flash.events.EventDispatcher;

import mx.collections.ArrayCollection;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class TimelineDataService extends EventDispatcher implements IReportDataService {

    private var dataRemoteSource:RemoteObject;

    public function TimelineDataService() {
        dataRemoteSource = new RemoteObject();
        dataRemoteSource.destination = "data";
        dataRemoteSource.list.addEventListener(ResultEvent.RESULT, processListData);
        dataRemoteSource.list.addEventListener(FaultEvent.FAULT, GenericFaultHandler.genericFault);
    }

    private var _preserveValues:Boolean = true;

    public function set preserveValues(value:Boolean):void {
        _preserveValues = value;
    }

    private var _obfuscate:Boolean;

    public function get obfuscate():Boolean {
        return _obfuscate;
    }

    public function set obfuscate(value:Boolean):void {
        _obfuscate = value;
    }

    private function processListData(event:ResultEvent):void {

        var seriesData:SeriesDataResults = dataRemoteSource.list.lastResult as SeriesDataResults;
        /*if (obfuscate) {
            new Obfuscator().obfuscate(seriesData);
        }*/
        var dataSets:ArrayCollection = new ArrayCollection();
        for each (var listData:ListDataResults in seriesData.listDatas) {
            var listServ:ListDataService = new ListDataService();
            listServ.preserveValues = false;
            dataSets.addItem(listServ.translate(listData));
        }
        seriesData.additionalProperties.seriesValues = seriesData.seriesValues;
        dispatchEvent(new DataServiceEvent(DataServiceEvent.DATA_RETURNED, dataSets, seriesData.dataSourceInfo,
                seriesData.additionalProperties, seriesData.auditMessages, seriesData.reportFault, false, 0, 0));
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STOPPED));
    }

    public function retrieveData(definition:AnalysisDefinition, refreshAllSources:Boolean):void {
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STARTED));
        var metadata:InsightRequestMetadata = new InsightRequestMetadata();
        metadata.utcOffset = new Date().getTimezoneOffset();
        metadata.refreshAllSources = refreshAllSources;
        dataRemoteSource.list.send(definition, metadata);
    }
}
}