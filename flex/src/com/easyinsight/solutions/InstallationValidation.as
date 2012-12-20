/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/13/12
 * Time: 8:27 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.solutions {
[Bindable]
[RemoteClass(alias="com.easyinsight.solutions.InstallationValidation")]
public class InstallationValidation {

    public var existingConnectionID:int;
    public var atSizeLimit:Boolean;
    public var enterpriseLimit:Boolean;

    public function InstallationValidation() {
    }
}
}
