package com.easyinsight.analysis {
import mx.collections.ArrayCollection;
import mx.containers.Form;
import mx.events.FlexEvent;

public class AnalysisItemDetailEditor extends Form {

    private var _analysisItem:AnalysisItem;

    private var _dimensionFields:ArrayCollection;
    private var _dateDimensionFields:ArrayCollection;
    private var _measureObjects:ArrayCollection;
    private var _measureFields:ArrayCollection;

    public function AnalysisItemDetailEditor() {
        super();
    }

    [Bindable]
    public function get measureFields():ArrayCollection {
        return _measureFields;
    }

    public function set measureFields(value:ArrayCollection):void {
        if (_measureFields == value) return;
        _measureFields = value;
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }

    [Bindable]
    public function get measureObjects():ArrayCollection {
        return _measureObjects;
    }

    public function set measureObjects(val:ArrayCollection):void {
        _measureObjects = val;
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }

    [Bindable]
    public function get dimensionFields():ArrayCollection {
        return _dimensionFields;
    }

    public function set dimensionFields(val:ArrayCollection):void {
        _dimensionFields = val;
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }

    [Bindable]
    public function get dateDimensionFields():ArrayCollection {
        return _dateDimensionFields;
    }

    public function set dateDimensionFields(val:ArrayCollection):void {
        _dateDimensionFields = val;
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }

    public function createAnalysisItem(aggregation:String):AnalysisItem {
        return null;
    }

    public function set analysisItem(val:AnalysisItem):void {
        _analysisItem = val;
    }

    public function get analysisItem():AnalysisItem {
        return _analysisItem;
    }

    public function save(analysisItem:AnalysisItem):void {
    }

    public function validate():Boolean {
        return true;
    }
}
}