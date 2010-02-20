package com.easyinsight.kpi {
import com.easyinsight.kpi.KPI;

import flash.events.Event;

public class KPIEvent extends Event {

    public static const KPI_ADDED:String = "kpiAdded";
    public static const KPI_EDITED:String = "kpiEdited";
    public static const KPI_REMOVED:String = "kpiRemoved";

    public var kpi:KPI;
    public var previousKPI:KPI;

    public function KPIEvent(type:String, kpi:KPI, previousKPI:KPI = null) {
        super(type, true);
        this.kpi = kpi;
        this.previousKPI = previousKPI;
    }

    override public function clone():Event {
        return new KPIEvent(type, kpi, previousKPI);
    }
}
}