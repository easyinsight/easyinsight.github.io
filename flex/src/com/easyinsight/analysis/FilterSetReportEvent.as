/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 3/6/13
 * Time: 2:01 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {

import flash.events.Event;

import mx.collections.ArrayCollection;

public class FilterSetReportEvent extends Event {

    public static const ADD_FILTER_SET:String = "addonFilterSet";
    public static const NEW_FILTER_SETS:String = "newFilterSets";

    public var filterSet:FilterSetDescriptor;

    public var addonFilterSets:ArrayCollection;
    public var removedFilterSets:ArrayCollection;

    public function FilterSetReportEvent(type:String, filterSet:FilterSetDescriptor, addonFilterSets:ArrayCollection = null, removedFilterSets:ArrayCollection = null) {
        super(type);
        this.filterSet = filterSet;
        this.addonFilterSets = addonFilterSets;
        this.removedFilterSets = removedFilterSets;
    }

    override public function clone():Event {
        return new FilterSetReportEvent(type, filterSet, addonFilterSets, removedFilterSets);
    }
}
}
