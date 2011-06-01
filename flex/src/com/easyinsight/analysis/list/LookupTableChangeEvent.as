/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 5/25/11
 * Time: 6:53 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.list {
import flash.events.Event;

public class LookupTableChangeEvent extends Event {

    public static const LOOKUP_TABLE_CHANGE:String = "";

    public function LookupTableChangeEvent() {
        super(LOOKUP_TABLE_CHANGE, true);
    }

    override public function clone():Event {
        return new LookupTableChangeEvent();
    }
}
}
