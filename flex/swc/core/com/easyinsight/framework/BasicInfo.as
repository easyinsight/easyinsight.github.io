/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 8/16/11
 * Time: 8:42 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.framework {
[Bindable]
[RemoteClass(alias="com.easyinsight.users.BasicInfo")]
public class BasicInfo {

    public var version:String;
    public var prefix:String;

    public function BasicInfo() {
    }
}
}
