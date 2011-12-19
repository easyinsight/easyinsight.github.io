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

    private var _sortValue:Object;

    public function SortableSparkGridColumn(reportIndex:int, sortValue:Object) {
        _reportIndex = reportIndex;
        _sortValue = sortValue;
    }


    public function get reportIndex():int {
        return _reportIndex;
    }

    public function get sortValue():Object {
        return _sortValue;
    }
}
}
