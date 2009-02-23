package com.adobe.flex.extras.controls.springgraph {
import flash.events.Event;
import flash.events.Event;
public class NewLinkEvent extends Event{

    public static const NEW_LINK:String = "newLink";

    public var edge1ID:String;
    public var edge2ID:String;

    public function NewLinkEvent(edge1ID:String, edge2ID:String) {
        super(NEW_LINK);
        this.edge1ID = edge1ID;
        this.edge2ID = edge2ID;
    }


    override public function clone():Event {
        return new NewLinkEvent(edge1ID, edge2ID);
    }
}
}