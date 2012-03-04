/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 2/29/12
 * Time: 2:18 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.framework {
[Bindable]
[RemoteClass(alias="com.easyinsight.users.TimerResponse")]
public class TimerResponse {

    public var rerunReport:Boolean;
    public var basicInfo:BasicInfo;

    public function TimerResponse() {
    }
}
}
