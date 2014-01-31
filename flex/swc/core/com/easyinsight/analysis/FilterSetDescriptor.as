/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 1/21/14
 * Time: 11:30 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import com.easyinsight.quicksearch.EIDescriptor;
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.FilterSetDescriptor")]
public class FilterSetDescriptor extends EIDescriptor{

    public var dataSourceID:int;

    public function FilterSetDescriptor() {
    }

    override public function getType():int {
        return EIDescriptor.FILTER_SET;
    }

    override public function get typeString():String {
        return "Filter Set";
    }
}
}
