/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 5/16/11
 * Time: 10:12 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.util {
[Bindable]
[RemoteClass(alias="com.easyinsight.audit.ActionLog")]
public class ActionLog {

    public var actionType:int;

    public function ActionLog() {
    }

    public function get display():String {
        return null;
    }

    public function get icon():Class {
        return null;
    }
}
}
