/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 3/29/11
 * Time: 10:21 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.account {
[Bindable]
[RemoteClass(alias="com.easyinsight.users.ExternalLogin")]
public class ExternalLogin {

    public var externalLoginID:int;

    public function ExternalLogin() {
    }
}
}
