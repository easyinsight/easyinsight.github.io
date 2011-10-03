/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 8/30/11
 * Time: 11:01 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.util {
import flash.events.Event;

public class FilterChangeEvent extends Event {

    public static const FILTER_CHANGE:String = "filterChange";

    public var selectedValue:Object;

    public function FilterChangeEvent(selectedValue:Object) {
        super(FILTER_CHANGE);
        this.selectedValue = selectedValue;
    }

    override public function clone():Event {
        return new FilterChangeEvent(selectedValue);
    }
}
}
