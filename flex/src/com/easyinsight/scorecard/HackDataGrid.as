package com.easyinsight.scorecard {
import mx.controls.DataGrid;

public class HackDataGrid extends DataGrid {
    public function HackDataGrid() {
        super();
    }

    public function get listRendererArray():Array {
        return listItems;
    }
}
}