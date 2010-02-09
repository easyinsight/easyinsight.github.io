package com.easyinsight.analysis {
import flash.events.Event;

import mx.collections.ArrayCollection;
import mx.core.IFactory;
import mx.core.UIComponent;

public class ReportEditorFieldControlsFactory extends UIComponent implements IFactory {

    private var _fields:ArrayCollection;

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

    public function newInstance():* {
        var controls:ReportEditorFieldControls = new ReportEditorFieldControls();
        controls.analysisItems = fields;
        return controls;
    }
}
}