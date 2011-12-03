/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/3/11
 * Time: 10:17 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.service {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.DataServiceEvent;
import com.easyinsight.analysis.IReportDataService;
import com.easyinsight.analysis.ytd.CompareYearsDataResults;
import com.easyinsight.framework.DataServiceLoadingEvent;
import com.easyinsight.framework.GenericFaultHandler;
import com.easyinsight.framework.InsightRequestMetadata;

import flash.events.EventDispatcher;

import mx.rpc.events.FaultEvent;

import mx.rpc.events.ResultEvent;

import mx.rpc.remoting.RemoteObject;

public class CompareYearsDataService extends EventDispatcher implements IReportDataService {

    private var dataRemoteSource:RemoteObject;

    public function CompareYearsDataService() {
        super();
        dataRemoteSource = new RemoteObject();
        dataRemoteSource.destination = "data";
        dataRemoteSource.getCompareYearsResults.addEventListener(ResultEvent.RESULT, processListData);
        dataRemoteSource.getCompareYearsResults.addEventListener(FaultEvent.FAULT, GenericFaultHandler.genericFault);
    }

    private var _preserveValues:Boolean = true;

    public function set preserveValues(value:Boolean):void {
        _preserveValues = value;
    }

    private function processListData(event:ResultEvent):void {
        var listData:CompareYearsDataResults = dataRemoteSource.getCompareYearsResults.lastResult as CompareYearsDataResults;
        dispatchEvent(new DataServiceEvent(DataServiceEvent.DATA_RETURNED, listData.dataSet, listData.dataSourceInfo, listData.additionalProperties, listData.auditMessages,
                listData.reportFault, false, 0, 0, listData.suggestions));
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STOPPED));
    }

    private var report:AnalysisDefinition;

    public function retrieveData(definition:AnalysisDefinition, refreshAll:Boolean):void {
        this.report = definition;
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STARTED));
        var metadata:InsightRequestMetadata = new InsightRequestMetadata();
        metadata.utcOffset = new Date().getTimezoneOffset();
        dataRemoteSource.getCompareYearsResults.send(definition, metadata);
    }
}
}
