package com.easyinsight.dashboard {
import com.easyinsight.quicksearch.EIDescriptor;

import flash.events.Event;

public class AirDescriptorEvent extends Event{

    public static const DESCRIPTOR_SELECTION:String = "descriptorSelection";

    public var descriptor:EIDescriptor;

    public function AirDescriptorEvent(descriptor:EIDescriptor) {
        super(DESCRIPTOR_SELECTION);
        this.descriptor = descriptor;
    }

    override public function clone():Event {
        return new AirDescriptorEvent(descriptor);
    }
}
}