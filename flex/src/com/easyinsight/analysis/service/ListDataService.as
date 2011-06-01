package com.easyinsight.analysis.service {

import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.DataServiceEvent;
import com.easyinsight.analysis.IReportDataService;
import com.easyinsight.analysis.ListDataResults;
import com.easyinsight.analysis.Value;
import com.easyinsight.framework.DataServiceLoadingEvent;
import com.easyinsight.framework.GenericFaultHandler;
import com.easyinsight.framework.InsightRequestMetadata;
import com.easyinsight.framework.InvalidFieldsEvent;

import flash.events.EventDispatcher;
import mx.collections.ArrayCollection;
import mx.controls.Alert;
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

    private var _preserveValues:Boolean = true;

    public function set preserveValues(value:Boolean):void {
        _preserveValues = value;
    }

    public function translate(listData:ListDataResults):ServiceData {
        var headers:ArrayCollection = new ArrayCollection(listData.headers);
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
                if (_preserveValues) {
                    endObject[key] = value;
                } else {
                    endObject[key] = value.getValue();
                }
                if (value.links != null) {
                    for (var linkKey:String in value.links) {
                        endObject[linkKey + "_link"] = value.links[linkKey];
                    }
                }
            }
            data.addItem(endObject);
        }
        return new ServiceData(data);
    }

    private function processListData(event:ResultEvent):void {
        var listData:ListDataResults = dataRemoteSource.list.lastResult as ListDataResults;
        if (listData.invalidAnalysisItemIDs != null && listData.invalidAnalysisItemIDs.length > 0) {
            dispatchEvent(new InvalidFieldsEvent(listData.invalidAnalysisItemIDs, listData.feedMetadata));
        }
        var serviceData:ServiceData = translate(listData);
        dispatchEvent(new DataServiceEvent(DataServiceEvent.DATA_RETURNED, serviceData.data, 
                listData.dataSourceInfo, listData.additionalProperties, listData.auditMessages, listData.reportFault,
                listData.limitedResults, listData.maxResults, listData.limitResults));
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STOPPED));
    }

    private function retrieve():void {
        
    }

    public function retrieveData(definition:AnalysisDefinition, refreshAllSources:Boolean):void {
        dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STARTED));
        var metadata:InsightRequestMetadata = new InsightRequestMetadata();
        metadata.utcOffset = new Date().getTimezoneOffset();
        metadata.refreshAllSources = refreshAllSources;
        metadata.reportEditor = true;
        dataRemoteSource.list.send(definition, metadata);
    }
}
}