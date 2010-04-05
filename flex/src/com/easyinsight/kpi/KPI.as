package com.easyinsight.kpi {
import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisMeasure;

import flash.events.Event;
import flash.events.EventDispatcher;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.kpi.KPI")]
public class KPI extends EventDispatcher {

    public var kpiID:int;
    public var coreFeedID:int;
    public var coreFeedName:String;
    public var analysisMeasure:AnalysisMeasure;
    public var dateDimension:AnalysisDateDimension;

    public var name:String;
    public var description:String;
    public var iconImage:String;

    public var kpiOutcome:KPIOutcome;
    public var goalDefined:Boolean;
    public var goalValue:Number = 0;
    public var highIsGood:int;

    public var dayWindow:int;
    public var threshold:Number = 0;

    public var problemConditions:ArrayCollection = new ArrayCollection();

    public var temporary:Boolean;

    public var reports:ArrayCollection;
    public var packages:ArrayCollection;
    public var kpiTrees:ArrayCollection;

    public var connectionVisible:Boolean;

    public var tags:ArrayCollection = new ArrayCollection();
    public var filters:ArrayCollection;
    public var objectives:ArrayCollection;

    public var kpiUsers:ArrayCollection;

    public var connectionID:int;

    public function KPI() {
    }

    private var _configurationMode:Boolean;


    [Bindable(event="configurationModeChanged")]
    public function get configurationMode():Boolean {
        return _configurationMode;
    }

    public function set configurationMode(value:Boolean):void {
        if (_configurationMode == value) return;
        _configurationMode = value;
        dispatchEvent(new Event("configurationModeChanged"));
    }

    private function blah():void {
        var kpiUser:KPIUser;
    }
}
}