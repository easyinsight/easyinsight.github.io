package com.easyinsight.icons {
import flash.events.Event;
public class IconSelectionEvent extends Event {
    public static const ICON_SELECTION:String = "iconSelection";

    public var icon:Icon;


    public function IconSelectionEvent(icon:Icon) {
        super(ICON_SELECTION, true);
        this.icon = icon;
    }


    override public function clone():Event {
        return new IconSelectionEvent(icon);
    }
}
}