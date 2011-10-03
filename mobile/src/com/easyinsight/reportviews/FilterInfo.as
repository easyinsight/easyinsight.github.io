/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 6/30/11
 * Time: 10:21 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.reportviews {
import com.easyinsight.filtering.FilterDefinition;

public class FilterInfo {

    public var filterDefinition:FilterDefinition;
    public var dataSourceID:int;
    public var reportID:int;
    public var dashboardID:int;

    public function FilterInfo(filterDefinition:FilterDefinition, dataSourceID:int, reportID:int, dashboardID:int) {
        this.filterDefinition = filterDefinition;
        this.dataSourceID = dataSourceID;
        this.reportID = reportID;
        this.dashboardID = dashboardID;
    }
}
}
