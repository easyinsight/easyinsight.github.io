/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 12/17/10
 * Time: 12:03 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.google {
[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.google.QuickbaseResponse")]
public class QuickbaseResponse {

    public var errorMessage:String;
    public var sessionTicket:String;

    public function QuickbaseResponse() {
    }
}
}
