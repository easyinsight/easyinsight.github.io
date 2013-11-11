/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 11/11/13
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {
import flash.events.Event;

import mx.collections.ArrayCollection;

public class MultiFieldEvent extends Event {

    public static const MULTI_FIELD_UPDATE:String = "multiFieldUpdate";

    public var fields:ArrayCollection;

    public function MultiFieldEvent(fields:ArrayCollection) {
        super(MULTI_FIELD_UPDATE);
        this.fields = fields;
    }

    override public function clone():Event {
        return new MultiFieldEvent(fields);
    }
}
}
