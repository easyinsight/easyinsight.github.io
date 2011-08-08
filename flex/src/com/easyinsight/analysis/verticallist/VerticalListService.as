/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/14/11
 * Time: 9:01 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.verticallist {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.DataResults;
import com.easyinsight.analysis.DataServiceEvent;
import com.easyinsight.analysis.IReportDataService;
import com.easyinsight.analysis.ListDataResults;
import com.easyinsight.analysis.ListDataResults;
import com.easyinsight.analysis.service.ListDataService;
import com.easyinsight.datasources.DataSourceInfo;
import com.easyinsight.framework.DataServiceLoadingEvent;
import com.easyinsight.framework.GenericFaultHandler;
import com.easyinsight.framework.InsightRequestMetadata;

import flash.events.EventDispatcher;

import mx.collections.ArrayCollection;

import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;

import mx.rpc.remoting.RemoteObject;

public class VerticalListService extends ListDataService implements IReportDataService {

    private var dataRemoteSource:RemoteObject;

    public function VerticalListService() {
        dataRemoteSource = new RemoteObject();
        dataRemoteSource.destination = "data";
        dataRemoteSource.getVerticalDataResults.addEventListener(ResultEvent.RESULT, processListData);
        dataRemoteSource.getVerticalDataResults.addEventListener(FaultEvent.FAULT, GenericFaultHandler.genericFault);
    }

    private var report:AnalysisDefinition;

    override public function retrieveData(definition:AnalysisDefinition, refreshAllSources:Boolean):void {
        this.report = definition;
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STARTED));
        var metadata:InsightRequestMetadata = new InsightRequestMetadata();
        metadata.utcOffset = new Date().getTimezoneOffset();
        dataRemoteSource.getVerticalDataResults.send(definition, metadata);
    }

    private function processListData(event:ResultEvent):void {
        var verticalResults:VerticalDataResults = dataRemoteSource.getVerticalDataResults.lastResult as VerticalDataResults;
        var results:ArrayCollection = new ArrayCollection();
        for (var i:int = 0; i < verticalResults.map.length; i++) {
            var dataResults:ListDataResults = verticalResults.map.getItemAt(i) as ListDataResults;
            results.addItem(translate(dataResults, report).data);
        }
        dispatchEvent(new DataServiceEvent(DataServiceEvent.DATA_RETURNED, results, ListDataResults(verticalResults.map.getItemAt(0)).dataSourceInfo,
                new Object(), new ArrayCollection(), null, false, 0, 0));
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STOPPED));
    }
}
}
