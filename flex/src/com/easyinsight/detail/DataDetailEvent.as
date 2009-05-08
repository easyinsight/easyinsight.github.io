package com.easyinsight.detail {
import com.easyinsight.filtering.FilterRawData;

import flash.events.Event;

public class DataDetailEvent extends Event {

    public static const DATA_DETAIL:String = "dataDetail";

    public var filterRawData:FilterRawData;

    public function DataDetailEvent(filterRawData:FilterRawData) {
        super(DATA_DETAIL, true);
        this.filterRawData = filterRawData;
    }

    override public function clone():Event {
        return new DataDetailEvent(filterRawData);
    }
}
}