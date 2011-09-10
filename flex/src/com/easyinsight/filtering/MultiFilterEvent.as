/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/7/11
 * Time: 3:45 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {
import flash.events.Event;

public class MultiFilterEvent extends Event {

    public static const MULTI_FILTER:String = "multiFilter";

    public var multiFilterOption:MultiFilterOption;

    public function MultiFilterEvent(multiFilterOption:MultiFilterOption) {
        super(MULTI_FILTER, true);
        this.multiFilterOption = multiFilterOption;
    }

    override public function clone():Event {
        return new MultiFilterEvent(multiFilterOption);
    }
}
}
