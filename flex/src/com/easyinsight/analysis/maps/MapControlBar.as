package com.easyinsight.analysis.maps {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItemUpdateEvent;
import com.easyinsight.analysis.CustomChangeEvent;
import com.easyinsight.analysis.DimensionDropArea;
import com.easyinsight.analysis.IReportControlBar;
import com.easyinsight.analysis.ListDropAreaGrouping;
import com.easyinsight.analysis.MeasureDropArea;
import com.easyinsight.analysis.ReportDataEvent;
import com.easyinsight.map.MapDropAreaGrouping;
import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.controls.ComboBox;
import mx.events.ListEvent;
public class MapControlBar extends HBox implements IReportControlBar {

    private var xAxisGrouping:ListDropAreaGrouping;
    private var measureGrouping:MapDropAreaGrouping;
    private var mapComboBox:ComboBox;
    private var mapDefinition:MapDefinition;

    public function MapControlBar() {
        xAxisGrouping = new ListDropAreaGrouping();
        xAxisGrouping.maxElements = 1;
        xAxisGrouping.dropAreaType = DimensionDropArea;
        xAxisGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        measureGrouping = new MapDropAreaGrouping();
        measureGrouping.maxElements = 1;
        measureGrouping.dropAreaType = MeasureDropArea;
        measureGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        mapComboBox = new ComboBox();
        mapComboBox.dataProvider = new ArrayCollection([
                 {label:"USA", data:MapDefinition.USA}, {label:"World", data:MapDefinition.WORLD},
                    {label:"Europe", data:MapDefinition.EUROPE}, {label:"Asia", data:MapDefinition.ASIA},
                    {label:"Americas", data:MapDefinition.AMERICAS}, {label:"Middle East", data:MapDefinition.MIDDLE_EAST}
        ]);
        mapComboBox.rowCount = 6;
        mapComboBox.addEventListener(ListEvent.CHANGE, onMapTypeChange);
    }

    private function onMapTypeChange(event:ListEvent):void {
        mapDefinition.mapType = mapComboBox.selectedItem.data;
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(mapComboBox);
        addChild(xAxisGrouping);
        addChild(measureGrouping);
         if (mapDefinition.geography != null) {
            xAxisGrouping.addAnalysisItem(mapDefinition.geography);
        }
        if (mapDefinition.measure != null) {
            measureGrouping.addAnalysisItem(mapDefinition.measure);
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

    public function set analysisItems(analysisItems:ArrayCollection):void {
        xAxisGrouping.analysisItems = analysisItems;
        measureGrouping.analysisItems = analysisItems;
    }

    public function isDataValid():Boolean {
        return (xAxisGrouping.getListColumns().length > 0 && measureGrouping.getListColumns().length > 0);
    }

    public function addItem(analysisItem:com.easyinsight.analysis.AnalysisItem):void {
    }

    public function onCustomChangeEvent(event:CustomChangeEvent):void {
    }
}
}