package com.easyinsight.analysis {
import flash.events.Event;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.core.Container;

public class ReportControlBar extends HBox {

    private var _analysisItems:ArrayCollection;
    private var _dataSourceID:int;

    public function ReportControlBar() {
        super();
        setStyle("verticalAlign", "middle");
    }

    public function set dataSourceID(dataSourceID:int):void {
        _dataSourceID = dataSourceID;
    }

    protected function addDropAreaGrouping(grouping:ListDropAreaGrouping, parent:Container = null):void {
        BindingUtils.bindProperty(grouping, "analysisItems", this, "analysisItems");
        grouping.dataSourceID = _dataSourceID;
        if (parent == null) {
            addChild(grouping);
        } else {
            parent.addChild(grouping);
        }
    }

    [Bindable(event="analysisItemsChanged")]
    public function get analysisItems():ArrayCollection {
        return _analysisItems;
    }

    public function set analysisItems(value:ArrayCollection):void {
        if (_analysisItems == value) return;
        _analysisItems = value;
        dispatchEvent(new Event("analysisItemsChanged"));
    }
}
}