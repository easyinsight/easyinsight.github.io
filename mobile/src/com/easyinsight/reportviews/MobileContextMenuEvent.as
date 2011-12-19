/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/14/11
 * Time: 10:09 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.reportviews {
import flash.events.Event;

public class MobileContextMenuEvent extends Event {
    
    public static const MOBILE_CONTEXT_SELECT:String = "mobileContextSelect";
    
    public function MobileContextMenuEvent() {
        super(MOBILE_CONTEXT_SELECT);
    }
}
}
