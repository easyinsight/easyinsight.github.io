/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 11/13/12
 * Time: 6:09 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.util {
import flash.events.Event;

public class RolloverLabelEvent extends Event {

    public static const ROLLOVER_LABEL:String = "rolloverLabel";

    public var data:Object;

    public function RolloverLabelEvent(data:Object) {
        super(ROLLOVER_LABEL, true);
        this.data = data;
    }

    override public function clone():Event {
        return new RolloverLabelEvent(data);
    }
}
}
