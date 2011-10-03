/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 8/25/11
 * Time: 1:28 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.reportviews {
import spark.components.gridClasses.GridColumn;

public class SortableSparkGridColumn extends GridColumn {

    private var _reportIndex:int;

    private var _sortValue:int;

    public function SortableSparkGridColumn(reportIndex:int, sortValue:int) {
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
