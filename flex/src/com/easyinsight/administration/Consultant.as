/**
 * Created by jamesboe on 9/19/14.
 */
package com.easyinsight.administration {
[Bindable]
[RemoteClass(alias="com.easyinsight.users.Consultant")]
public class Consultant {

    public var consultantName:String;
    public var account:String;

    public function Consultant() {
    }
}
}
