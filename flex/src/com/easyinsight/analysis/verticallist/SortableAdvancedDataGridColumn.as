/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/15/11
 * Time: 2:20 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.verticallist {
import mx.controls.advancedDataGridClasses.AdvancedDataGridColumn;

public class SortableAdvancedDataGridColumn extends AdvancedDataGridColumn {

    private var _reportIndex:int;

    private var _sortValue:int;

    public function SortableAdvancedDataGridColumn(reportIndex:int, sortValue:int) {
        _reportIndex = reportIndex;
        _sortValue = sortValue;
    }


    public function get reportIndex():int {
        return _reportIndex;
    }

    public function get sortValue():int {
        return _sortValue;
    }
}
}
