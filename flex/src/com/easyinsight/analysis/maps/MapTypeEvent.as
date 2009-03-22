package com.easyinsight.analysis.maps {
import com.easyinsight.analysis.CustomChangeEvent;
import flash.events.Event;
public class MapTypeEvent extends CustomChangeEvent{

    public var mapType:int;

    public function MapTypeEvent(mapType:int) {
        super();
        this.mapType = mapType;
    }


    override public function clone():Event {
        return new MapTypeEvent(mapType);
    }
}
}