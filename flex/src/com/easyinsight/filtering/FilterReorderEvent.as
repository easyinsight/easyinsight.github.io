/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 2/6/12
 * Time: 3:42 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {
import flash.events.Event;

import mx.collections.ArrayCollection;

public class FilterReorderEvent extends Event {

    public static const FILTER_REORDER:String = "filterReorder";

    public var filters:ArrayCollection;

    public function FilterReorderEvent(filters:ArrayCollection) {
        super(FILTER_REORDER);
        this.filters = filters;
    }

    override public function clone():Event {
        return new FilterReorderEvent(filters);
    }
}
}
