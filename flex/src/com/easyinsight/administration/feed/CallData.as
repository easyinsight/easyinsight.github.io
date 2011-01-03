/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 1/3/11
 * Time: 9:23 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.administration.feed {
[Bindable]
[RemoteClass(alias="com.easyinsight.util.CallData")]
public class CallData {

    public static const RUNNING:int = 1;
    public static const DONE:int = 2;
    public static const FAILED:int = 3;

    public var status:int;
    public var statusMessage:String;
    public var result:Object;

    public function CallData() {
    }
}
}
