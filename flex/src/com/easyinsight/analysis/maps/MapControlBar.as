package com.easyinsight.analysis.maps {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.AnalysisItemUpdateEvent;
import com.easyinsight.analysis.CustomChangeEvent;
import com.easyinsight.analysis.DataServiceEvent;
import com.easyinsight.analysis.DimensionDropArea;
import com.easyinsight.analysis.IReportControlBar;
import com.easyinsight.analysis.ListDropAreaGrouping;
import com.easyinsight.analysis.MeasureDropArea;
import com.easyinsight.analysis.ReportControlBar;
import com.easyinsight.analysis.ReportDataEvent;
import com.easyinsight.map.MapDropAreaGrouping;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.controls.Label;
import mx.events.FlexEvent;
import mx.events.ListEvent;

public class MapControlBar extends ReportControlBar implements IReportControlBar {

    private var xAxisGrouping:ListDropAreaGrouping;
    private var measureGrouping:MapDropAreaGrouping;
    private var mapDefinition:MapDefinition;

    private static const WORLD:Object = {label:"World", data:MapDefinition.WORLD};
    private static const USA:Object = { label:"USA", data:MapDefinition.USA};
    private static const EUROPE:Object = {label:"Europe", data:MapDefinition.EUROPE};
    private static const ASIA:Object = {label:"Asia", data:MapDefinition.ASIA};
    private static const AMERICAS:Object = {label:"Americas", data:MapDefinition.AMERICAS};
    private static const MIDDLE_EAST:Object = {label:"Middle East", data:MapDefinition.MIDDLE_EAST};

    private var lookupMap:Object = new Object();

    public function MapControlBar() {
        xAxisGrouping = new ListDropAreaGrouping();
        xAxisGrouping.maxElements = 1;
        xAxisGrouping.dropAreaType = DimensionDropArea;
        xAxisGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        measureGrouping = new MapDropAreaGrouping();
        measureGrouping.maxElements = 1;
        measureGrouping.dropAreaType = MeasureDropArea;
        measureGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        lookupMap[MapDefinition.WORLD] = WORLD;
        lookupMap[MapDefinition.USA] = USA;
        lookupMap[MapDefinition.EUROPE] = EUROPE;
        lookupMap[MapDefinition.ASIA] = ASIA;
        lookupMap[MapDefinition.AMERICAS] = AMERICAS;
        lookupMap[MapDefinition.MIDDLE_EAST] = MIDDLE_EAST;
    }

    private function onMapTypeChange(event:ListEvent):void {
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
    }

    override protected function createChildren():void {
        super.createChildren();
        addDropAreaGrouping(xAxisGrouping);
        addDropAreaGrouping(measureGrouping);
         if (mapDefinition.geography != null) {
            xAxisGrouping.addAnalysisItem(mapDefinition.geography);
        }
        if (mapDefinition.measure != null) {
            measureGrouping.addAnalysisItem(mapDefinition.measure);
        }
        var limitLabel:Label = new Label();
        BindingUtils.bindProperty(limitLabel, "text", this, "limitText");
        addChild(limitLabel);
    }

    private var _limitText:String;

    [Bindable]
    public function get limitText():String {
        return _limitText;
    }

    public function set limitText(val:String):void {
        _limitText = val;
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }

    public function onDataReceipt(event:DataServiceEvent):void {
        if (event.limitedResults) {
            limitText = "Showing " + event.limitResults + " of " + event.maxResults + " results";
        } else {
            limitText = "";
        }
    }

    private function requestListData(event:AnalysisItemUpdateEvent):void {
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
    }

    public function set analysisDefinition(analysisDefinition:AnalysisDefinition):void {
        mapDefinition = analysisDefinition as MapDefinition;        
    }

    public function createAnalysisDefinition():AnalysisDefinition {
        mapDefinition.geography = xAxisGrouping.getListColumns()[0];
        mapDefinition.measure = measureGrouping.getListColumns()[0];
        return mapDefinition;
    }

    public function isDataValid():Boolean {
        return (xAxisGrouping.getListColumns().length > 0 && measureGrouping.getListColumns().length > 0);
    }

    public function addItem(analysisItem:AnalysisItem):void {
        if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
            measureGrouping.addAnalysisItem(analysisItem);
        } else if (analysisItem.hasType(AnalysisItemTypes.DIMENSION)) {
            xAxisGrouping.addAnalysisItem(analysisItem);
        }
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
    }

    public function onCustomChangeEvent(event:CustomChangeEvent):void {
    }
}
}