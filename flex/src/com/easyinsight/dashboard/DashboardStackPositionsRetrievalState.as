/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/30/13
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.dashboard {
import com.easyinsight.analysis.IRetrievalState;
import com.easyinsight.filtering.FilterDefinition;
import com.easyinsight.filtering.FilterMetadata;
import com.easyinsight.solutions.InsightDescriptor;

public class DashboardStackPositionsRetrievalState implements IRetrievalState{

    private var dashboardStackPositions:DashboardStackPositions;

    public function DashboardStackPositionsRetrievalState(dashboardStackPositions:DashboardStackPositions) {
        this.dashboardStackPositions = dashboardStackPositions;
    }

    public function forFilter(filterDefinition:FilterDefinition, key:String, overridenFilters:Object):void {
        var overrideFilter:FilterDefinition = overridenFilters[String(filterDefinition.filterID)];
        if (overrideFilter != null) {
            filterDefinition.loadFromSharedObject(overrideFilter.getSaveValue());
            filterDefinition.enabled = overrideFilter.enabled;
        }
    }

    public function updateFilter(filterDefinition:FilterDefinition, filterMetadata:FilterMetadata):void {
    }

    public function getStackPosition(dashboardStackKey:String):int {
        return dashboardStackPositions.getStackPosition(dashboardStackKey);
    }

    public function saveStackPosition(dashboardStackKey:String, index:int):void {
    }

    public function getReport(dashboardReportKey:String):InsightDescriptor {
        return dashboardStackPositions.getReport(dashboardReportKey);
    }

    public function saveReport(dashboardReportKey:String, report:InsightDescriptor):void {
    }
}
}
