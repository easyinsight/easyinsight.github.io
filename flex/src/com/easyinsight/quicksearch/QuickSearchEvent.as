package com.easyinsight.quicksearch {
import flash.events.Event;
public class QuickSearchEvent extends Event{

    public static const QUICK_SEARCH:String = "quickSearch";
    public static const QUICK_SEARCH_CANCEL:String = "searchCancel";

    public var eiDescriptor:EIDescriptor;

    public function QuickSearchEvent(type:String, eiDescriptor:EIDescriptor = null) {
        super(type);
        this.eiDescriptor = eiDescriptor;
    }

    override public function clone():Event {
        return new QuickSearchEvent(type, eiDescriptor);
    }
}
}