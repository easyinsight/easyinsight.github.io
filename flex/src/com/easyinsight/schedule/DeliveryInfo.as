/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 6/8/11
 * Time: 2:21 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.schedule {
[Bindable]
[RemoteClass(alias="com.easyinsight.export.DeliveryInfo")]
public class DeliveryInfo {

    public static const REPORT:int = 1;
    public static const SCORECARD:int = 2;

    public var name:String;
    public var id:int;
    public var index:int;
    public var type:int;
    public var format:int;

    public function DeliveryInfo() {
    }
}
}
