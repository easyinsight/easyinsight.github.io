/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 3/12/12
 * Time: 2:42 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.rowedit {
import flash.events.Event;

public class ActualRowEvent extends Event {

    public static const ACTUAL_ROW_DATA:String = "actualRowData";

    public var actualRowSet:ActualRowSet;

    public function ActualRowEvent(actualRowSet:ActualRowSet) {
        super(ACTUAL_ROW_DATA, true);
        this.actualRowSet = actualRowSet;
    }

    override public function clone():Event {
        return new ActualRowEvent(actualRowSet);
    }
}
}