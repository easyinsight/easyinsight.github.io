package com.easyinsight.customupload {

import com.easyinsight.solutions.DataSourceDescriptor;

import flash.events.Event;

public class DataSourceConfiguredEvent extends Event{

    public static const DATA_SOURCE_CONFIGURED:String = "dataSourceConfigured";

    public var descriptor:DataSourceDescriptor;
    public var requiresFieldSetup:Boolean;

    public function DataSourceConfiguredEvent(descriptor:DataSourceDescriptor, requiresFieldSetup:Boolean = false) {
        super(DATA_SOURCE_CONFIGURED);
        this.descriptor = descriptor;
        this.requiresFieldSetup = requiresFieldSetup;
    }

    override public function clone():Event {
        return new DataSourceConfiguredEvent(descriptor, requiresFieldSetup);
    }
}
}