/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/18/12
 * Time: 10:30 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {
import flash.events.Event;

public class CustomRollingIntervalEvent extends Event {

    public static const FILTER_ADDED:String = "customFilterIntervalAdded";
    public static const FILTER_EDITED:String = "customFilterIntervalEdited";
    public static const FILTER_DELETED:String = "customFilterIntervalDeleted";

    public var interval:CustomRollingInterval;

    public function CustomRollingIntervalEvent(type:String, interval:CustomRollingInterval) {
        super(type);
        this.interval = interval;
    }

    override public function clone():Event {
        return new CustomRollingIntervalEvent(type, interval);
    }
}
}
