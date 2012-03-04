/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 2/29/12
 * Time: 2:19 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.framework {
[Bindable]
[RemoteClass(alias="com.easyinsight.users.TimerRequest")]
public class TimerRequest {

    public var dataSourceID:int;
    public var date:Date;

    public function TimerRequest() {
    }
}
}
