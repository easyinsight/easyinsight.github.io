/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 8/1/11
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.preferences {
import flash.events.Event;

public class DLSEvent extends Event {

    public static const DLS_DELETE:String = "dlsDelete";

    public var dls:DataSourceDLS;

    public function DLSEvent(dls:DataSourceDLS) {
        super(DLS_DELETE,  true);
        this.dls = dls;
    }

    override public function clone():Event {
        return new DLSEvent(dls);
    }
}
}
