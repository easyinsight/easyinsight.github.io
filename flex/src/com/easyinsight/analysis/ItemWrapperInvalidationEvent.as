package com.easyinsight.analysis {
import flash.events.Event;

public class ItemWrapperInvalidationEvent extends Event {

    public static const ITEM_INVALIDATION:String = "itemValidation";

    public function ItemWrapperInvalidationEvent() {
        super(ITEM_INVALIDATION);
    }


    override public function clone():Event {
        return new ItemWrapperInvalidationEvent();
    }
}
}