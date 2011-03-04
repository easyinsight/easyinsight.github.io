/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 3/1/11
 * Time: 7:26 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import flash.events.Event;

import mx.collections.ArrayCollection;

public class JoinOverrideEvent extends Event {

    public static const JOIN_OVERRIDE_SET:String = "joinOverrideSet";

    public var joins:ArrayCollection;

    public function JoinOverrideEvent(type:String, joins:ArrayCollection) {
        super(type);
        this.joins = joins;
    }

    override public function clone():Event {
        return new JoinOverrideEvent(type, joins);
    }
}
}
