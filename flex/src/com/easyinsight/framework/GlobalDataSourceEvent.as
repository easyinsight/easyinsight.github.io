/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 2/29/12
 * Time: 2:21 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.framework {
import flash.events.Event;

public class GlobalDataSourceEvent extends Event {

    public static const GLOBAL_DATA_SOURCE:String = "globalDataSource";

    public function GlobalDataSourceEvent() {
        super(GLOBAL_DATA_SOURCE);
    }

    override public function clone():Event {
        return new GlobalDataSourceEvent();
    }
}
}
