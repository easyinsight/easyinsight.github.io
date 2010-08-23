package com.easyinsight.framework {
import com.easyinsight.listing.AnalyzeSource;

import flash.events.Event;

public class ConnectionGoEvent extends Event {

    public static const CONNECTION_GO:String = "connectionGo";

    public var perspectiveInfo:PerspectiveInfo;

    public function ConnectionGoEvent(perspectiveInfo:PerspectiveInfo) {
        super(CONNECTION_GO);
        this.perspectiveInfo = perspectiveInfo;
    }

    override public function clone():Event {
        return new ConnectionGoEvent(perspectiveInfo);
    }
}
}