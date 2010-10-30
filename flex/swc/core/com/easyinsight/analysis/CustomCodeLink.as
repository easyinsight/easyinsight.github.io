package com.easyinsight.analysis {

import flash.events.Event;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.CustomCodeLink")]
public class CustomCodeLink extends Link{
    
    public function CustomCodeLink() {
        super();
    }

    override public function get type():String {
        return "Custom Code";
    }

    public function createEvent(data:Object):Event {
        return null;
    }
}
}