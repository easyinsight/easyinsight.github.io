package com.easyinsight.analysis {
import flash.events.Event;

public class AddedItemUpdateEvent extends Event {

    public static const UPDATE:String = "addedItemUpdate";
    public static const REMOVE:String = "addedItemRemove";

    public var previousItem:AnalysisItem;
    public var newItem:AnalysisItem;
    public var wrapper:AnalysisItemWrapper;

    public function AddedItemUpdateEvent(type:String, previousItem:AnalysisItem, wrapper:AnalysisItemWrapper, newItem:AnalysisItem = null) {
        super(type, true);
        this.previousItem = previousItem;
        this.wrapper = wrapper;
        this.newItem = newItem;
    }

    override public function clone():Event {
        return new AddedItemUpdateEvent(type, previousItem, wrapper, newItem);
    }
}
}