package com.easyinsight.dashboard {
import flash.events.Event;

public class GridDimensionEvent extends Event {

    public static const GRID_DIMENSION:String = "gridDimension";

    public var rows:int;
    public var columns:int;
    public var changed:Boolean = false;

    public function GridDimensionEvent(rows:int, columns:int, changed:Boolean = false) {
        super(GRID_DIMENSION);
        this.rows = rows;
        this.columns = columns;
        this.changed = changed;
    }

    override public function clone():Event {
        return new GridDimensionEvent(rows, columns, changed);
    }
}
}