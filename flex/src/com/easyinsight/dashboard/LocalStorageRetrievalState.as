/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/30/13
 * Time: 9:18 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.dashboard {
import com.easyinsight.analysis.IRetrievalState;
import com.easyinsight.filtering.FilterDefinition;
import com.easyinsight.filtering.FilterMetadata;
import com.easyinsight.solutions.InsightDescriptor;

import flash.net.SharedObject;

import mx.collections.ArrayCollection;
import mx.controls.Alert;

public class LocalStorageRetrievalState implements IRetrievalState {

    private var urlKey:String;
    private var version:int;

    private var retrievalState:IRetrievalState;

    public function LocalStorageRetrievalState(urlKey:String, version:int, retrievalState:IRetrievalState = null) {
        this.urlKey = urlKey;
        this.version = version;
        this.retrievalState = retrievalState;
    }

    public function forFilter(filterDefinition:FilterDefinition, key:String, overridenFilters:Object):void {
        if (retrievalState != null) {
            return retrievalState.forFilter(filterDefinition, key, overridenFilters);
        }
        try {
            var so:SharedObject = SharedObject.getLocal("d" + urlKey + version);
            if (so.size > 0) {
                var obj:Object = so.data[key + "f" + filterDefinition.filterID];
                if (obj != null) {
                    filterDefinition.loadFromSharedObject(obj);
                    filterDefinition.enabled = so.data[key + "f" + filterDefinition.filterID + "enabled"];
                }
            }
            so.flush();
        } catch (e:Error) {

        }
    }

    public function updateFilter(filterDefinition:FilterDefinition, filterMetadata:FilterMetadata):void {
        try {
            var key:String = filterMetadata.key;
            var so:SharedObject = SharedObject.getLocal("d" + urlKey + version);
            so.data[key + "f" + filterDefinition.filterID] = filterDefinition.getSaveValue();
            so.data[key + "f" + filterDefinition.filterID + "enabled"] = filterDefinition.enabled;
            so.flush();
        } catch (e:Error) {

        }
    }

    public function saveStackPosition(dashboardStackKey:String, index:int):void {
        try {
            var so:SharedObject = SharedObject.getLocal("d" + urlKey);
            so.data.version = version;
            so.data["de" + dashboardStackKey] = index;
            so.flush();
        } catch (e:Error) {
        }
    }

    public function getStackPosition(dashboardStackKey:String):int {
        if (retrievalState != null) {
            return retrievalState.getStackPosition(dashboardStackKey);
        }
        try {
            var so:SharedObject = SharedObject.getLocal("d" + urlKey);
            if (so.size > 0) {
                var savedVersion:int = so.data.version;
                if (savedVersion == version) {
                    var stackPosition:int = so.data["de" + dashboardStackKey];
                    if (stackPosition > 0) {
                        return stackPosition;
                    }
                } else {
                    so.clear();
                }
            }
            so.flush();
        } catch (e:Error) {
        }
        return -1;
    }

    public function getReport(dashboardReportKey:String):InsightDescriptor {
        if (retrievalState != null) {
            return retrievalState.getReport(dashboardReportKey);
        }
        try {
            var so:SharedObject = SharedObject.getLocal("d" + urlKey + version);
            if (so.size > 0) {
                var customReport:InsightDescriptor = so.data["r" + dashboardReportKey];
                if (customReport != null) {
                    return customReport;
                }
            }
        } catch (e:Error) {

        }
        return null;
    }

    public function saveReport(dashboardReportKey:String, report:InsightDescriptor):void {
        try {
            var so:SharedObject = SharedObject.getLocal("d" + urlKey);
            so.data.version = version;
            so.data["r" + dashboardReportKey] = report;
            so.flush();
        } catch (e:Error) {
        }
    }
}
}
