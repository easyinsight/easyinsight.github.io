package com.easyinsight {
import com.easyinsight.analysis.AnalysisHierarchyItem;

import com.easyinsight.analysis.DropArea;
import com.easyinsight.analysis.DropAreaDragUpdateCommand;

import com.easyinsight.analysis.HierarchyLevel;
import com.easyinsight.commands.CommandEvent;

import flash.events.Event;

import mx.controls.ComboBox;

public class HierarchyComboBox extends ComboBox{

    private var _hierarchy:AnalysisHierarchyItem;
    private var _dropArea:DropArea;

    public function HierarchyComboBox() {
        super();
        labelField = "display";
        addEventListener(Event.CHANGE, onChange);
    }

    private function onChange(event:Event):void {
        _hierarchy.hierarchyLevel = selectedItem as HierarchyLevel;
        dispatchEvent(new CommandEvent(new DropAreaDragUpdateCommand(_dropArea, _hierarchy, _hierarchy)));
    }

    public function set hierarchy(val:AnalysisHierarchyItem):void {
        _hierarchy = val;
    }

    public function set dropArea(value:DropArea):void {
        _dropArea = value;
    }

    override protected function commitProperties():void {
        super.commitProperties();
        dataProvider = _hierarchy.hierarchyLevels;
        selectedItem = _hierarchy.hierarchyLevel;
    }
}
}