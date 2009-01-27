package com.easyinsight.feedassembly {
import mx.collections.ArrayCollection;
import flash.events.Event;
public class JoinSelectionEvent extends Event {
    public static const JOIN_SELECTION:String = "joinSelection";

    public var addedJoins:ArrayCollection;
    public var removedJoins:ArrayCollection;

    public function JoinSelectionEvent(addedJoins:ArrayCollection, removedJoins:ArrayCollection = null) {
        super(JOIN_SELECTION);
        this.addedJoins = addedJoins;
        this.removedJoins = removedJoins;
    }


    override public function clone():Event {
        return new JoinSelectionEvent(addedJoins, removedJoins);
    }
}
}