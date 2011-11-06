/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/27/11
 * Time: 3:08 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.listing {
import com.easyinsight.solutions.DataSourceDescriptor;

import flash.events.Event;

public class DataSourceEvent extends Event {

    public static const NAVIGATE_TO_DATA_SOURCE:String = "navigateToDataSource";

    public var descriptor:DataSourceDescriptor;

    public function DataSourceEvent(type:String, descriptor:DataSourceDescriptor) {
        super(type, true);
        this.descriptor = descriptor;
    }

    override public function clone():Event {
        return new DataSourceEvent(type, descriptor);
    }
}
}
