package com.easyinsight.analysis {
import com.easyinsight.scorecard.*;

import flash.events.Event;

public class DataSourceRefreshEvent extends Event {

    public static const DATA_SOURCE_REFRESH:String = "dataSourceRefresh";

    public function DataSourceRefreshEvent() {
        super(DATA_SOURCE_REFRESH);
    }

    override public function clone():Event {
        return new DataSourceRefreshEvent();
    }
}
}