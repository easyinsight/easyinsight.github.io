package com.easyinsight.dashboard {
import com.easyinsight.genredata.AnalyzeEvent;
import com.easyinsight.genredata.ModuleAnalyzeEvent;
import com.easyinsight.goals.GoalDataAnalyzeSource;
import com.easyinsight.listing.AnalyzeSource;

import com.easyinsight.quicksearch.EIDescriptor;

import flash.events.Event;
import flash.events.EventDispatcher;

import mx.collections.ArrayCollection;

public class AnalyzeEventAdapter extends EventDispatcher {

    private var _descriptors:ArrayCollection;

    public function AnalyzeEventAdapter() {
    }


    [Bindable(event="descriptorsChanged")]
    public function get descriptors():ArrayCollection {
        return _descriptors;
    }

    public function set descriptors(value:ArrayCollection):void {
        if (_descriptors == value) return;
        _descriptors = value;
        dispatchEvent(new Event("descriptorsChanged"));
    }

    public function translateAnalyze(event:AnalyzeEvent):void {
        var source:AnalyzeSource = event.analyzeSource;
        if (source is GoalDataAnalyzeSource) {
            var goalSource:GoalDataAnalyzeSource = source as GoalDataAnalyzeSource;
            var desc:EIDescriptor = findDescriptor(EIDescriptor.GOAL_TREE, goalSource.goalTreeID, descriptors);
            if (desc != null) {
                dispatchEvent(new AirDescriptorEvent(desc));
            }
        }
    }

    private function findDescriptor(type:int, id:int, descriptors:ArrayCollection):EIDescriptor {
        var desc:EIDescriptor = null;
        for each (var descriptor:EIDescriptor in descriptors) {
            if (descriptor.getType() == type && descriptor.id == id) {
                desc = descriptor;
            }
        }
        return desc;
    }

    public function translateModuleAnalyze(event:ModuleAnalyzeEvent):void {

    }
}
}