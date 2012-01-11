/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 1/9/12
 * Time: 4:26 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.list {
import flash.events.Event;

public class SizeOverrideEvent extends Event {
    
    public static const SIZE_OVERRIDE:String = "sizeOverride";
    
    public var width:int;
    public var height:int;
    
    public function SizeOverrideEvent(width:int = -1, height:int = -1) {
        super(SIZE_OVERRIDE, true);
        this.width = width;
        this.height = height;
    }

    override public function clone():Event {
        return new SizeOverrideEvent(width, height);
    }
}
}
