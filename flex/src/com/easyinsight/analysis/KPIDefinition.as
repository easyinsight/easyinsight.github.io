/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/5/11
 * Time: 1:59 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import com.easyinsight.filtering.FilterDefinition;
import com.easyinsight.filtering.RollingDateRangeFilterDefinition;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSKPIDefinition")]
public class KPIDefinition extends AnalysisDefinition {

    public var measures:ArrayCollection;
    public var filterName:String;
    public var groupings:ArrayCollection;
    public var dayWindow:int = 30;

    public function KPIDefinition() {
    }

    override public function cleanupReport(filterDefinitions:ArrayCollection):ArrayCollection {
        return new ArrayCollection();
        // keep it from removing intrinsic trend filter
    }

    override public function newFilters(filterDefinitions:ArrayCollection):ArrayCollection {
        var toAdd:ArrayCollection = new ArrayCollection();
        if (filterName == null) {
            var filter:RollingDateRangeFilterDefinition;
            for each (var filterDef:FilterDefinition in filterDefinitions) {
                if (filterDef.intrinsic && filterDef.getType() == FilterDefinition.ROLLING_DATE) {
                    filter = filterDef as RollingDateRangeFilterDefinition;
                    break;
                }
            }
            if (filter == null) {
                var tempField:AnalysisDateDimension = new AnalysisDateDimension();
                tempField.displayName = "Date";
                var key:NamedKey = new NamedKey();
                key.name = "Date";
                tempField.key = key;

                filter = new RollingDateRangeFilterDefinition();
                filter.intrinsic = true;
                filter.trendFilter = true;
                filter.filterName = "Trend Date";
                filter.interval = RollingDateRangeFilterDefinition.WEEK;
                filter.field = tempField;
                toAdd.addItem(filter);
            }
            if (filter.filterName == null || filter.filterName == "") {
                filterName = filter.field.display;
            } else {
                filterName = filter.filterName;
            }

        }
        return toAdd;
    }
}
}
