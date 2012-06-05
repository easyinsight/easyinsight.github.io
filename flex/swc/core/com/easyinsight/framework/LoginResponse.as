/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 3/25/12
 * Time: 6:46 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.framework {
[Bindable]
[RemoteClass(alias="com.easyinsight.users.LoginResponse")]
public class LoginResponse {

    public var userServiceResponse:UserServiceResponse;
    public var token:String;
    public var userIDString:String;

    public function LoginResponse() {
    }
}
}
