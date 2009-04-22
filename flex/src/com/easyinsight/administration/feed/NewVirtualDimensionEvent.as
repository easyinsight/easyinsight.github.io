package com.easyinsight.administration.feed {
import flash.events.Event;

public class NewVirtualDimensionEvent extends Event{

    public static const NEW_VIRTUAL_DIMENSION:String = "newVirtualDimension";

    public var dimension:VirtualDimension;

    public function NewVirtualDimensionEvent(dimension:VirtualDimension) {
        super(NEW_VIRTUAL_DIMENSION);
        this.dimension = dimension;
    }

    override public function clone():Event {
        return new NewVirtualDimensionEvent(dimension);
    }
}
}