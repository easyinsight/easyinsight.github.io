/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/28/13
 * Time: 3:02 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import com.easyinsight.filtering.FilterDefinition;
import com.easyinsight.filtering.FilterMetadata;
import com.easyinsight.solutions.InsightDescriptor;

public interface IRetrievalState {
    function forFilter(filterDefinition:FilterDefinition, key:String, overridenFilters:Object):void;
    function updateFilter(filterDefinition:FilterDefinition, filterMetadata:FilterMetadata):void;

    function getStackPosition(dashboardStackKey:String):int;
    function saveStackPosition(dashboardStackKey:String, index:int):void;
    function getReport(dashboardReportKey:String):InsightDescriptor;
    function saveReport(dashboardReportKey:String, report:InsightDescriptor):void;
}
}
