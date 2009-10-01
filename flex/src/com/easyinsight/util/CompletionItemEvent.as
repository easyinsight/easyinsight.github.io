package com.easyinsight.util {

import flash.events.Event;

public class CompletionItemEvent extends Event{

    public static const ITEM_CHANGED:String = "itemChanged";

    public var selectedItem:Object;

    public function CompletionItemEvent(type:String, selectedItem:Object) {
        super(type);
        this.selectedItem = selectedItem;
    }

    override public function clone():Event {
        return new CompletionItemEvent(type, selectedItem);
    }
}
}