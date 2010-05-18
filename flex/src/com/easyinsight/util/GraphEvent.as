package com.easyinsight.util {
import flash.events.Event;

public class GraphEvent extends Event {

    public static const CONNECT:String = "connectGraph";
    public static const DELETE:String = "deleteGraph";

    public var node:GraphFeedNode;

    public function GraphEvent(type:String, node:GraphFeedNode) {
        super(type, true);
        this.node = node;
    }

    override public function clone():Event {
        return new GraphEvent(type, node);
    }
}
}