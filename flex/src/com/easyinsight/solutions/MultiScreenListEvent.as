package com.easyinsight.solutions {
import flash.events.Event;

import mx.collections.ArrayCollection;

public class MultiScreenListEvent extends Event {

    public static const MULTI_SCREEN:String = "multiScreen";

    public var descriptors:ArrayCollection;

    public function MultiScreenListEvent(descriptors:ArrayCollection) {
        super(MULTI_SCREEN);
        this.descriptors = descriptors;
    }

    override public function clone():Event {
        return new MultiScreenListEvent(descriptors);
    }
}
}