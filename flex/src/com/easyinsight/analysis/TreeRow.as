/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/5/12
 * Time: 2:29 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import com.easyinsight.analysis.summary.NewSummaryRow;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.TreeRow")]
public class TreeRow {

    public var values:Object;
    public var children:ArrayCollection;
    public var groupingColumn:Value;
    public var sortColumn:Value;
    public var groupingField:AnalysisItem;
    public var summaryColumn:Boolean;
    public var stupidFlex:int;
    public var newSummaryRow:NewSummaryRow;
    public var backgroundColor:uint;
    public var textColor:uint;

    public function TreeRow() {
    }

    public function sortValue():Object {
        if (sortColumn != null && sortColumn.type() != Value.EMPTY) {
            return sortColumn.getValue();
        }
        return groupingColumn.getValue();
    }
}
}
