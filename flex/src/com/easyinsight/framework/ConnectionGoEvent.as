package com.easyinsight.framework {
import com.easyinsight.listing.AnalyzeSource;

import flash.events.Event;

public class ConnectionGoEvent extends Event {

    public static const CONNECTION_GO:String = "connectionGo";

    public var source:AnalyzeSource;

    public function ConnectionGoEvent(source:AnalyzeSource) {
        super(CONNECTION_GO);
        this.source = source;
    }

    override public function clone():Event {
        return new ConnectionGoEvent(source);
    }
}
}