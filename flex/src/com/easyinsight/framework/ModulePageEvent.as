package com.easyinsight.framework {
import com.easyinsight.listing.IPerspective;

import flash.events.Event;

public class ModulePageEvent extends Event{

    public static const MODULE_PAGE_LOADED:String = "modulePageLoaded";

    public var perspective:IPerspective;
    public var screen:FrontScreen;

    public function ModulePageEvent(perspective:IPerspective, screen:FrontScreen) {
        super(MODULE_PAGE_LOADED);
        this.perspective = perspective;
        this.screen = screen;
    }

    override public function clone():Event {
        return new ModulePageEvent(perspective, screen);
    }
}
}