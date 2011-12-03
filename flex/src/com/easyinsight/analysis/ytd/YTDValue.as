/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/2/11
 * Time: 6:07 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.ytd {
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.Value;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.YTDValue")]
public class YTDValue {
    
    public var analysisMeasure:AnalysisMeasure;
    public var benchmarkMeasure:AnalysisMeasure;
    public var ytd:Value;
    public var average:Value;
    public var benchmarkValue:Value;
    public var variation:Value;
    public var timeIntervalValues:ArrayCollection;
    
    public function YTDValue() {
    }
}
}
