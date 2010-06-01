package com.easyinsight.analysis {
import mx.collections.SortField;

public class OrderedSortField extends SortField {

    private var _order:int;

    public function OrderedSortField() {
        super();
    }

    public function get order():int {
        return _order;
    }

    public function set order(value:int):void {
        _order = value;
    }
}
}