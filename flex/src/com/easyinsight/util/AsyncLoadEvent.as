package com.easyinsight.util {
import flash.events.Event;

public class AsyncLoadEvent extends Event{

    public static const LOAD_START:String = "asyncLoadStart";
    public static const LOAD_SUCCESS:String = "asyncLoadSuccess";
    public static const LOAD_FAILURE:String = "asyncLoadFailure";

    public var asyncScreen:IAsyncScreen;

    public function AsyncLoadEvent(type:String, asyncScreen:IAsyncScreen = null) {
        super(type);
        this.asyncScreen = asyncScreen;
    }

    override public function clone():Event {
        return new AsyncLoadEvent(type, asyncScreen);
    }
}
}