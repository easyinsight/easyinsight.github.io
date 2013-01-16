/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 6/27/11
 * Time: 2:55 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.datasources {
import flash.events.Event;

public class DataSourceRefreshEvent extends Event {

    public static const DATA_SOURCE_REFRESH:String = "dataSourceRefreshed";

    public var newDateTime:Date;
    public var newFields:Boolean;

    public function DataSourceRefreshEvent(newDateTime:Date, newFields:Boolean) {
        super(DATA_SOURCE_REFRESH);
        this.newDateTime = newDateTime;
        this.newFields = newFields;
    }

    override public function clone():Event {
        return new DataSourceRefreshEvent(newDateTime, newFields);
    }
}
}
