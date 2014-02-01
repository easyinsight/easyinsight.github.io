/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 1/30/14
 * Time: 9:13 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.schedule {
public class NeverScheduleType extends ScheduleType {

    public function NeverScheduleType() {
    }

    override public function get interval():String {
        return "Never (Disabled)";
    }
}
}
