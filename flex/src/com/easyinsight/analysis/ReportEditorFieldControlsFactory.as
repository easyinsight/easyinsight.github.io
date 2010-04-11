package com.easyinsight.analysis {
import flash.events.Event;

import mx.collections.ArrayCollection;
import mx.core.IFactory;
import mx.core.UIComponent;

public class ReportEditorFieldControlsFactory extends UIComponent implements IFactory {

    private var _fields:ArrayCollection;
    private var _dataSourceID:int;

    public function ReportEditorFieldControlsFactory() {
    }

    [Bindable(event="fieldsChanged")]
    public function get fields():ArrayCollection {
        return _fields;
    }

    public function set fields(value:ArrayCollection):void {
        if (_fields == value) return;
        _fields = value;
        dispatchEvent(new Event("fieldsChanged"));
    }

    public function set dataSourceID(value:int):void {
        _dataSourceID = value;
    }

    public function newInstance():* {
        var controls:ReportEditorFieldControls = new ReportEditorFieldControls();
        controls.analysisItems = fields;
        controls.dataSourceID = _dataSourceID;
        return controls;
    }
}
}