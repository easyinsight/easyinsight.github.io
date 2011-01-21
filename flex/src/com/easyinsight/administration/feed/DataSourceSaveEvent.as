/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 1/20/11
 * Time: 6:55 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.administration.feed {
import flash.events.Event;

public class DataSourceSaveEvent extends Event {

    public static const DATA_SOURCE_SAVE:String = "dataSourceSave";

    public function DataSourceSaveEvent() {
        super(DATA_SOURCE_SAVE);
    }

    override public function clone():Event {
        return new DataSourceSaveEvent();
    }
}
}
