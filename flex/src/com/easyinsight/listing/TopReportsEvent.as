/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 2/6/13
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.listing {
import flash.events.Event;

import mx.collections.ArrayCollection;

public class TopReportsEvent extends Event {

    public static const TOP_REPORTS:String = "topReports";

    public var descriptors:ArrayCollection;

    public function TopReportsEvent(descriptors:ArrayCollection) {
        super(TOP_REPORTS);
        this.descriptors = descriptors;
    }

    override public function clone():Event {
        return new TopReportsEvent(descriptors);
    }
}
}
