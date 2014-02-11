/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 1/17/14
 * Time: 10:33 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.summary {
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.Value;

public class NewSummaryRow {

    public var depth:int;
    public var backgroundColor:uint;
    public var textColor:uint;
    public var values:Object;
    public var groupingColumn:Value;
    public var groupingField:AnalysisItem;
    public var stupidFlex:int;
    public var parent:NewSummaryRow;
    public var summaryRow:Boolean;
    public var sortColumn:Value;

    public function NewSummaryRow() {
    }

    public function sortValue():Value {
        if (sortColumn != null) {
            return sortColumn;
        }
        return groupingColumn;
    }
}
}
