/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/1/13
 * Time: 4:31 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.dashboard {
import flash.events.Event;

import mx.collections.ArrayCollection;

public class DashboardFilterOverrideEvent extends Event {

    public static const DASHBOARD_FILTER_OVERRIDE:String = "dashboardFilterOverride";

    public var filters:ArrayCollection;

    public function DashboardFilterOverrideEvent(type:String, filters:ArrayCollection) {
        super(type);
        this.filters = filters;
    }

    override public function clone():Event {
        return new DashboardFilterOverrideEvent(type, filters);
    }
}
}
