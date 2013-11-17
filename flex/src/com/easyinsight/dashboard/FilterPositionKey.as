/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 11/15/13
 * Time: 8:24 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.dashboard {
import mx.controls.Alert;

public class FilterPositionKey {

    public static const DASHBOARD:int = 1;
    public static const REPORT:int = 2;
    public static const DASHBOARD_REPORT:int = 3;
    public static const DASHBOARD_STACK:int = 4;

    public var scope:int;
    public var filterID:int;
    public var scopeURLKey:String;

    public function FilterPositionKey() {
    }

    public static function createString(scope:int, filterID:int, scopeURLKey:String = null):String {
        var str:String = String(scope) + "|" + filterID;
        if (scopeURLKey != null) {
            str += ("|" + scopeURLKey);
        }
        return str;
    }
}
}
