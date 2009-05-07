package com.easyinsight.administration.feed {
import com.easyinsight.analysis.AggregationTypes;
import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.AnalysisItemWrapper;

import com.easyinsight.analysis.AnalysisMeasure;

import flash.events.Event;

import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.controls.ComboBox;

public class BatchFieldEditor extends HBox {

    private var wrapper:AnalysisItemWrapper;
    private var comboBox:ComboBox;

    public function BatchFieldEditor() {
        super();
        comboBox = new ComboBox();
        comboBox.dataProvider = new ArrayCollection([ "Grouping", "Date", "Sum", "Average",
            "Min", "Max", "Count"]);
        comboBox.addEventListener(Event.CHANGE, onChange);
    }

    private function onChange(event:Event):void {
        switch (comboBox.selectedItem) {
            case "Grouping":
                if (wrapper.analysisItem.getType() != AnalysisItemTypes.DIMENSION) {
                    var analysisDim:AnalysisDimension = new AnalysisDimension();
                }
                break;
            case "Date":
                break;
            case "Sum":
                break;
            case "Average":
                break;
            case "Min":
                break;
            case "Max":
                break;
            case "Count":
                break;
        }
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(comboBox);
    }

    override public function get data():Object {
        return wrapper;
    }

    override public function set data(value:Object):void {
        wrapper = value as AnalysisItemWrapper;
        if (wrapper.analysisItem.hasType(AnalysisItemTypes.DATE)) {
            comboBox.selectedItem = "Date";
        } else if (wrapper.analysisItem.hasType(AnalysisItemTypes.DIMENSION)) {
            comboBox.selectedItem = "Grouping";
        } else {
            var measure:AnalysisMeasure = wrapper.analysisItem as AnalysisMeasure;
            if (measure.aggregation == AggregationTypes.SUM) {
                comboBox.selectedItem = "Sum";
            } else if (measure.aggregation == AggregationTypes.AVERAGE) {
                comboBox.selectedItem = "Average";
            } else if (measure.aggregation == AggregationTypes.MIN) {
                comboBox.selectedItem = "Min";
            } else if (measure.aggregation == AggregationTypes.MAX) {
                comboBox.selectedItem = "Max";
            } else if (measure.aggregation == AggregationTypes.COUNT) {
                comboBox.selectedItem = "Count";
            }
        }
    }
}
}