package com.easyinsight.analysis {
import mx.controls.AdvancedDataGrid;
public class ListViewGrid extends AdvancedDataGrid {

    override public function findString(str:String):Boolean {
        return false;
    }
}
}