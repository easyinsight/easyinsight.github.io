package com.easyinsight.report {
import com.easyinsight.quicksearch.EIDescriptor;

import flash.events.Event;

import mx.collections.ArrayCollection;

public class ReportNavigationEvent extends Event{

    public static const TO_REPORT:String = "toReport";

    public var descriptor:EIDescriptor;
    public var filters:ArrayCollection;
    public var additionalItems:ArrayCollection;

    public function ReportNavigationEvent(type:String, descriptor:EIDescriptor, filters:ArrayCollection, additionalItems:ArrayCollection = null) {
        super(type, true);
        this.descriptor = descriptor;
        this.filters = filters;
        this.additionalItems = additionalItems;
    }

    override public function clone():Event {
        return new ReportNavigationEvent(type, descriptor, filters, additionalItems);
    }
}
}