package com.easyinsight.analysis.service {

import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.DataServiceEvent;
import com.easyinsight.analysis.IReportDataService;
import com.easyinsight.analysis.ListDataResults;
import com.easyinsight.analysis.Value;
import com.easyinsight.analysis.conditions.ConditionRenderer;
import com.easyinsight.framework.CredentialsCache;
import com.easyinsight.framework.DataServiceLoadingEvent;
import com.easyinsight.framework.GenericFaultHandler;
import com.easyinsight.framework.InsightRequestMetadata;
import com.easyinsight.framework.InvalidFieldsEvent;
import flash.events.EventDispatcher;
import mx.collections.ArrayCollection;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class ListDataService extends EventDispatcher implements IReportDataService {

    private var dataRemoteSource:RemoteObject;

    public function ListDataService() {
        super();
        dataRemoteSource = new RemoteObject();
        dataRemoteSource.destination = "data";
        dataRemoteSource.list.addEventListener(ResultEvent.RESULT, processListData);
        dataRemoteSource.list.addEventListener(FaultEvent.FAULT, GenericFaultHandler.genericFault);
    }

    private function processListData(event:ResultEvent):void {
        var listData:ListDataResults = dataRemoteSource.list.lastResult as ListDataResults;
        if (listData.invalidAnalysisItemIDs != null && listData.invalidAnalysisItemIDs.length > 0) {
            dispatchEvent(new InvalidFieldsEvent(listData.invalidAnalysisItemIDs, listData.feedMetadata));
        }
        var clientProcessorMap:Object = new Object();
        var headers:ArrayCollection = new ArrayCollection(listData.headers);        
        for each (var analysisItem:AnalysisItem in headers) {
            clientProcessorMap[analysisItem.qualifiedName()] = analysisItem.createClientRenderer();
        }
        var rows:ArrayCollection = new ArrayCollection(listData.rows);
        var data:ArrayCollection = new ArrayCollection();
        for (var i:int = 0; i < rows.length; i++) {
            var row:Object = rows.getItemAt(i);
            var values:Array = row.values as Array;
            var endObject:Object = new Object();
            for (var j:int = 0; j < headers.length; j++) {
                var headerDimension:AnalysisItem = headers[j];
                var value:Value = values[j];
                var key:String = headerDimension.qualifiedName();
                endObject[key] = value.getValue();
                var conditionRenderer:ConditionRenderer = clientProcessorMap[key];
                conditionRenderer.addValue(value);
            }
            data.addItem(endObject);
        }
        dispatchEvent(new DataServiceEvent(DataServiceEvent.DATA_RETURNED, data, clientProcessorMap, listData.limitedResults,
                listData.maxResults, listData.limitResults));
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STOPPED));
    }

    public function retrieveData(definition:AnalysisDefinition):void {
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STARTED));
        var metadata:InsightRequestMetadata = new InsightRequestMetadata();
        metadata.credentialFulfillmentList = CredentialsCache.getCache().createCredentials();
        dataRemoteSource.list.send(definition, false, metadata);
    }
}
}