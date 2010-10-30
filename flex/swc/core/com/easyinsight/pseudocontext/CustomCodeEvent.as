package com.easyinsight.pseudocontext {
import com.easyinsight.analysis.CustomCodeLink;

import flash.events.Event;

public class CustomCodeEvent extends Event {

    public static const CUSTOM_CODE_LINK:String = "customCodeLink";

    public var data:Object;
    public var link:CustomCodeLink;

    public function CustomCodeEvent(data:Object, link:CustomCodeLink) {
        super(CUSTOM_CODE_LINK, true);
        this.data = data;
        this.link = link;
    }

    override public function clone():Event {
        return new CustomCodeEvent(data, link);
    }
}
}