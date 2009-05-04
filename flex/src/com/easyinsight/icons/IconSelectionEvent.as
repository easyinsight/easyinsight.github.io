package com.easyinsight.icons {
import flash.events.Event;
public class IconSelectionEvent extends Event {
    public static const ICON_SELECTION:String = "iconSelection";
    public static const ICON_REMOVED:String = "iconRemoved";

    public var icon:Icon;

    public function IconSelectionEvent(type:String, icon:Icon) {
        super(type, true);
        this.icon = icon;
    }


    override public function clone():Event {
        return new IconSelectionEvent(type, icon);
    }
}
}