/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 3/29/11
 * Time: 10:22 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.account {
[Bindable]
[RemoteClass(alias="com.easyinsight.users.QuickbaseExternalLogin")]
public class QuickbaseExternalLogin extends ExternalLogin {

    public var hostName:String;
    public var quickbaseExternalLoginID:int;

    public function QuickbaseExternalLogin() {
    }
}
}
