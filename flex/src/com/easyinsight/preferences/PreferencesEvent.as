package com.easyinsight.preferences {

import flash.events.Event;

public class PreferencesEvent extends Event{

    public static const LAUNCH_PREFERENCES:String = "launchPreferences";

    public function PreferencesEvent() {
        super(LAUNCH_PREFERENCES, true);
    }

    override public function clone():Event {
        return new PreferencesEvent();
    }
}
}