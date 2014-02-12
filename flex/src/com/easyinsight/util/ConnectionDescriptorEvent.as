/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 2/5/14
 * Time: 11:51 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.util {
import com.easyinsight.quicksearch.EIDescriptor;

import flash.events.Event;

public class ConnectionDescriptorEvent extends Event {

    public static const CONNECTION_ADD:String = "connectionAdd";

    public var desc:EIDescriptor;

    public function ConnectionDescriptorEvent(type:String, desc:EIDescriptor) {
        super(CONNECTION_ADD);
        this.desc = desc;
    }

    override public function clone():Event {
        return new ConnectionDescriptorEvent(type, desc);
    }
}
}
