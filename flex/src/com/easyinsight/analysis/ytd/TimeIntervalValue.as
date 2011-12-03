/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/2/11
 * Time: 6:07 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.ytd {
import com.easyinsight.analysis.Value;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.TimeIntervalValue")]
public class TimeIntervalValue {
    
    public var dateValue:Value;
    public var value:Value;
    
    public function TimeIntervalValue() {
    }
}
}
