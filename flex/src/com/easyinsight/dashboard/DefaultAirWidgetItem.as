package com.easyinsight.dashboard {
import com.easyinsight.framework.DataServiceLoadingEvent;

import mx.core.Container;

public class DefaultAirWidgetItem extends AirWidgetItem{

    public function DefaultAirWidgetItem() {
        super();
        name = "Welcome";
    }


    override public function refreshData():void {
        displayObject.dispatchEvent(new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STOPPED));
    }

    override public function createDisplayObject():Container {
        return new DefaultAirScreen(); 
    }
}
}