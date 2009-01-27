package com.easyinsight.customupload.api {
import flash.events.Event;
public class EditMethodEvent extends Event {
    public static const EDIT_METHOD:String = "editMethod";

    public var configuredMethod:ConfiguredMethod;

    public function EditMethodEvent(configuredMethod:ConfiguredMethod) {
        super(EDIT_METHOD, true);
        this.configuredMethod = configuredMethod;
    }


    override public function clone():Event {
        return new EditMethodEvent(configuredMethod);
    }
}
}