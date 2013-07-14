/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/8/13
 * Time: 5:17 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.dashboard {
import flash.events.Event;

import mx.collections.ArrayCollection;

public class DashboardFilterEvent extends Event {

    public static const BLAH:String = "dashboardFilterOverrides";

    public var filters:ArrayCollection;

    public function DashboardFilterEvent(filters:ArrayCollection) {
        super(BLAH, true);
        this.filters = filters;
    }

    override public function clone():Event {
        return new DashboardFilterEvent(filters);
    }
}
}
