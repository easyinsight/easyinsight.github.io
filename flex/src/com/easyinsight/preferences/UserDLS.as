/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 8/1/11
 * Time: 8:15 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.preferences {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.preferences.UserDLS")]
public class UserDLS {

    public var dlsID:int;
    public var dataSourceName:String;
    public var dataSourceID:int;
    public var userDLSFilterList:ArrayCollection;

    public function UserDLS() {
    }
}
}
