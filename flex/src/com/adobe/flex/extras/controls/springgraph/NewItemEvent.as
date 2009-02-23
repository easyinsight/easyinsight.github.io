package com.adobe.flex.extras.controls.springgraph {
import flash.events.Event;
public class NewItemEvent extends Event{

    public static const NEW_ITEM:String = "newItem";

    public var item:Item;

    public function NewItemEvent(item:Item) {
        super(NEW_ITEM);
        this.item = item;
    }


    override public function clone():Event {
        return new NewItemEvent(item);
    }
}
}