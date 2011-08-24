/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/16/11
 * Time: 1:02 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.MonthCutoffFilter")]
public class MonthCutoffFilterDefinition extends FilterDefinition {

    public var dateLevel:int;

    public function MonthCutoffFilterDefinition() {
    }

    override public function getType():int {
        return FilterDefinition.MONTH_CUTOFF;
    }
}
}
