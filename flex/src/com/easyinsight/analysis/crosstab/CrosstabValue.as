/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/13/11
 * Time: 12:15 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.crosstab {
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.Value;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.CrosstabValue")]
public class CrosstabValue {

    public var value:Value;
    public var header:AnalysisItem;
    public var headerLabel:Boolean;
    public var summaryValue:Boolean;

    public function CrosstabValue() {
    }
}
}
