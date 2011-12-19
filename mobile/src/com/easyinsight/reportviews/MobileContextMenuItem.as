/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/14/11
 * Time: 10:07 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.reportviews {
import flash.events.EventDispatcher;

public class MobileContextMenuItem extends EventDispatcher {

    private var _label:String;

    public function MobileContextMenuItem(label:String) {
        _label = label;

    }


    public function get label():String {
        return _label;
    }


}
}
