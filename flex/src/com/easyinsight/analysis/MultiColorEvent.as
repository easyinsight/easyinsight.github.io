/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 1/22/13
 * Time: 1:47 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import flash.events.Event;

import mx.collections.ArrayCollection;

public class MultiColorEvent extends Event {

    public static const MULTI_COLOR:String = "multiColor";

    public var colors:ArrayCollection;

    public function MultiColorEvent(colors:ArrayCollection) {
        super(MULTI_COLOR);
        this.colors = colors;
    }

    override public function clone():Event {
        return new MultiColorEvent(colors);
    }
}
}
