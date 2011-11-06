package com.easyinsight.listing {
import com.easyinsight.quicksearch.EIDescriptor;

import flash.events.Event;
public class DeleteDataSourceEvent extends Event{

    public static const DELETE_DATA_SOURCE:String = "deleteDataSource";

    public var descriptor:EIDescriptor;

    public function DeleteDataSourceEvent(descriptor:EIDescriptor) {
        super(DELETE_DATA_SOURCE, true);
        this.descriptor = descriptor;
    }


    override public function clone():Event {
        return new DeleteDataSourceEvent(descriptor);
    }
}
}