package com.easyinsight.kpi {
import com.easyinsight.analysis.AnalysisMeasure;

import flash.events.EventDispatcher;

import mx.collections.ArrayCollection;

public class KPIData extends EventDispatcher {

    public var kpiName:String;
    public var dataSourceID:int;
    public var dataSourceName:String;
    public var measure:AnalysisMeasure;
    public var filters:ArrayCollection;
    public var highIsGood:int;
    public var dayWindow:int = 2;
    public var kpiIcon:String;

    public function KPIData() {
    }
}
}