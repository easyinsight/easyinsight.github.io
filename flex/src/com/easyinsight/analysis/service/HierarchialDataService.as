package com.easyinsight.analysis.service {

import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisHierarchyItem;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.DataServiceEvent;
import com.easyinsight.analysis.HierarchyLevel;
import com.easyinsight.analysis.IReportDataService;
import com.easyinsight.analysis.ListDataResults;
import com.easyinsight.analysis.Value;
import com.easyinsight.analysis.conditions.ConditionRenderer;
import com.easyinsight.framework.DataServiceLoadingEvent;
import com.easyinsight.framework.GenericFaultHandler;
import com.easyinsight.framework.InvalidFieldsEvent;
import flash.events.EventDispatcher;
import mx.collections.ArrayCollection;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class HierarchialDataService extends EventDispatcher implements IReportDataService {

    private var dataRemoteSource:RemoteObject;

    public function HierarchialDataService() {
        super();
        dataRemoteSource = new RemoteObject();
        dataRemoteSource.destination = "data";
        dataRemoteSource.list.addEventListener(ResultEvent.RESULT, processListData);
        dataRemoteSource.list.addEventListener(FaultEvent.FAULT, GenericFaultHandler.genericFault);
    }

    private function handleLevel(level:HierarchyLevel, levels:ArrayCollection, row:Object, measure1:AnalysisMeasure, measure2:AnalysisMeasure):Object {
        var result:Object = new Object();
        var value:Value = row[level.analysisItem.qualifiedName()];
        var valueObj:Object = value.getValue();
        result.label = String(valueObj);
        var index:int = levels.getItemIndex(level);
        if (index < (levels.length - 1)) {
            result.children = new ArrayCollection();
            var nextLevel:HierarchyLevel = levels.getItemAt(index + 1) as HierarchyLevel;
            result.children.addItem(handleLevel(nextLevel, levels, row, measure1, measure2));
        } else {
            var measure1Value:Value = row[measure1.qualifiedName()];
            var measure2Value:Value = row[measure2.qualifiedName()];
            result.area = measure1Value.getValue();
            result.color = measure2Value.getValue();
        }
        return result;
    }

    private function processListData(event:ResultEvent):void {
        var listData:ListDataResults = dataRemoteSource.list.lastResult as ListDataResults;
        if (listData.invalidAnalysisItemIDs != null && listData.invalidAnalysisItemIDs.length > 0) {
            dispatchEvent(new InvalidFieldsEvent(listData.invalidAnalysisItemIDs, listData.feedMetadata));
        }

        var hierarchy:AnalysisHierarchyItem;

        var rowMap:Object = new Object();

        for each (var rowObj:Object in listData.rows) {
            var translatedRowObj:Object = rowMap;
            for (var k:int = 0; k < hierarchy.hierarchyLevels.length; k++) {
                var level:HierarchyLevel = hierarchy.hierarchyLevels.getItemAt(k) as HierarchyLevel;
                var value:Value = rowObj[level.analysisItem.qualifiedName()];
                var valueObj:Object = value.getValue();
                var valueString:String = String(valueObj);
                if (k == 0) {
                    translatedRowObj["label"] = valueString;
                    var nextChild:Object = new Object();

                } else {

                }
            }
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
        dataRemoteSource.list.send(definition, false, null);
    }
}
}