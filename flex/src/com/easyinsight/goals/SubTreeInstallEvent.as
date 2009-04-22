package com.easyinsight.goals {
import flash.events.Event;

public class SubTreeInstallEvent extends Event{

    public static const SUB_TREE_INSTALL:String = "subTreeInstall";
    public static const SUB_TREE_UNINSTALL:String = "subTreeUnInstall";

    public var subTreeID:int;

    public function SubTreeInstallEvent(type:String, subTreeID:int) {
        super(type);
        this.subTreeID = subTreeID;
    }

    override public function clone():Event {
        return new SubTreeInstallEvent(type, subTreeID);
    }
}
}