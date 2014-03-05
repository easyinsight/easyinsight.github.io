/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/5/11
 * Time: 1:59 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import com.easyinsight.filtering.RollingDateRangeFilterDefinition;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSKPIDefinition")]
public class KPIDefinition extends AnalysisDefinition {

    public var measures:ArrayCollection;
    public var filterName:String;
    public var groupings:ArrayCollection;
    public var nowDate:String;
    public var previousDate:String;
    public var dayWindow:int = 30;

    public function KPIDefinition() {
    }

    override public function cleanupReport(filterDefinitions:ArrayCollection):ArrayCollection {
        return new ArrayCollection();
        // keep it from removing intrinsic trend filter
    }

    override public function newFilters(filterDefinitions:ArrayCollection):Object {
        var o:Object = new Object();
        var toAdd:ArrayCollection = new ArrayCollection();
        var fields:ArrayCollection = new ArrayCollection();
        if (filterName == null) {
            if (analysisID == 0 && baseDate == null) {
                //baseDate = "Date";
                nowDate = "Now";
                previousDate = "Against";
                var date:DerivedAnalysisDateDimension = new DerivedAnalysisDateDimension();
                date.concrete = false;
                date.applyBeforeAggregation = true;
                date.derivationCode = "nowdate()";
                var key:NamedKey = new NamedKey();
                key.name = "Date";
                date.key = key;
                fields.addItem(date);
                var filter1:RollingDateRangeFilterDefinition = new RollingDateRangeFilterDefinition();
                filter1.field = date;
                filter1.interval = RollingDateRangeFilterDefinition.ALL;
                filter1.filterName = "Now";
                toAdd.addItem(filter1);
                var filter2:RollingDateRangeFilterDefinition = new RollingDateRangeFilterDefinition();
                filter2.field = date;
                filter2.interval = RollingDateRangeFilterDefinition.ALL;
                filter2.filterName = "Against";
                toAdd.addItem(filter2);
            }
        }
        o["filters"] = toAdd;
        o["fields"] = fields;
        return o;
    }
}
}
