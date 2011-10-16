/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/27/11
 * Time: 10:16 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import com.easyinsight.analysis.trend.TrendDefinition;
import com.easyinsight.analysis.trend.TrendGridDefinition;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.TrendOutcome")]
public class TrendOutcome {

    public var now:Value;
    public var historical:Value;
    public var measure:AnalysisMeasure;
    public var dimensions:Object;
    public var direction:int;
    public var outcome:int;
    public var dataSourceID:int;
    public var leftTextWidth:int;
    public var report:TrendGridDefinition;

    public function TrendOutcome() {
    }

    public function get name():String {
        return measure.display;
    }

    public function get nowValue():Number {
        return now.toNumber();
    }

    public function get historicalValue():Number {
        return historical.toNumber();
    }

    public function get percentChange():Number {
        return (now.toNumber() - historical.toNumber()) / historical.toNumber() * 100;
    }
}
}
