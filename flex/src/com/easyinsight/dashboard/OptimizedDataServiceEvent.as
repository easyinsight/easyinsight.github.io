/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 8/16/11
 * Time: 5:13 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.dashboard {
import com.easyinsight.analysis.ReportFault;

import flash.events.Event;

public class OptimizedDataServiceEvent extends Event {

    public static const OPTIMIZE_RESULTS:String = "optimizedResults";

    public var resultsMap:Object;

    public var reportFault:ReportFault;

    public function OptimizedDataServiceEvent(resultMap:Object) {
        super(OPTIMIZE_RESULTS);
        this.resultsMap = resultMap;
    }
}
}
