/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/3/11
 * Time: 10:07 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.ytd {
import com.easyinsight.analysis.Value;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.CompareYearsResult")]
public class CompareYearsResults {

    public var value:Value;
    public var header:Value;
    public var percentChange:Boolean;
    
    public function CompareYearsResults() {
    }
}
}
