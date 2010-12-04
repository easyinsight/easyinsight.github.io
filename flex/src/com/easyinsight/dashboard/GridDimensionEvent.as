package com.easyinsight.dashboard {
import flash.events.Event;

public class GridDimensionEvent extends Event {

    public static const GRID_DIMENSION:String = "gridDimension";

    public var rows:int;
    public var columns:int;

    public function GridDimensionEvent(rows:int, columns:int) {
        super(GRID_DIMENSION);
        this.rows = rows;
        this.columns = columns;
    }

    override public function clone():Event {
        return new GridDimensionEvent(rows, columns);
    }
}
}