/**
 * Created by jamesboe on 9/12/14.
 */
package com.easyinsight.datasources {

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.infusionsoft.InfusionsoftUser")]

public class InfusionsoftUser {

    public var userID:String;
    public var email:String;

    public function InfusionsoftUser() {
    }
}
}
