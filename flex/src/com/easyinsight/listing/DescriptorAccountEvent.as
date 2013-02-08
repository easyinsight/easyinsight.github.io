/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 2/6/13
 * Time: 10:45 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.listing {
import com.easyinsight.quicksearch.EIDescriptor;

import flash.events.Event;

public class DescriptorAccountEvent extends Event {

    public static const ADD:String = "descriptorAdd";

    public var descriptor:EIDescriptor;

    public function DescriptorAccountEvent(type:String, descriptor:EIDescriptor) {
        super(type);
        this.descriptor = descriptor;
    }

    override public function clone():Event {
        return new DescriptorAccountEvent(type, descriptor);
    }
}
}
