package com.easyinsight.analysis.list {
import com.easyinsight.analysis.CustomChangeEvent;
import flash.events.Event;
public class ColumnReorderEvent extends CustomChangeEvent{

    public var columns:Array;

    public function ColumnReorderEvent(columns:Array) {
        super();
        this.columns = columns;
    }


    override public function clone():Event {
        return new ColumnReorderEvent(columns);
    }
}
}