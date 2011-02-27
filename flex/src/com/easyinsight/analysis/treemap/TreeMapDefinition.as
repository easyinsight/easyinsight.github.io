package com.easyinsight.analysis.treemap {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisHierarchyItem;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.ColorReportFormItem;
import com.easyinsight.analysis.ComboBoxReportFormItem;
import com.easyinsight.analysis.HierarchyDrilldownEvent;
import com.easyinsight.analysis.HierarchyLevel;

import com.easyinsight.filtering.FilterRawData;

import flash.events.Event;

import mx.collections.ArrayCollection;
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.WSTreeMapDefinition")]
public class TreeMapDefinition extends AnalysisDefinition{

    public function TreeMapDefinition() {
        super();
    }

    public var measure1:AnalysisItem;
    public var measure2:AnalysisItem;
    public var hierarchy:AnalysisItem;
    public var treeMapDefinitionID:int;

    public var highColor:uint = 0xAAAAFF;
    public var lowColor:uint = 0x333388;
    public var colorStrategy:String = "Linear";

    override public function get type():int {
        return AnalysisDefinition.TREEMAP;
    }

    override public function getFields():ArrayCollection {
        return new ArrayCollection([ measure1, measure2, hierarchy ]);
    }

    override public function fromSave(savedDef:AnalysisDefinition):void {
        super.fromSave(savedDef);
        this.treeMapDefinitionID = TreeMapDefinition(savedDef).treeMapDefinitionID;
    }

    override public function createFormItems():ArrayCollection {
        var items:ArrayCollection = super.createFormItems();
        items.addItem(new ComboBoxReportFormItem("Color Strategy", "colorStrategy", colorStrategy,
                this, ["Linear", "Logarithmic"]));
        items.addItem(new ColorReportFormItem("High Color", "highColor", highColor, this));
        items.addItem(new ColorReportFormItem("Low Color", "lowColor", lowColor, this));
        return items;
    }

    override public function populate(fields:ArrayCollection):void {
        var measures:ArrayCollection = findItems(fields, AnalysisItemTypes.MEASURE);
        if (measures.length > 0) {
            measure1 = measures.getItemAt(0) as AnalysisItem;
            if (measures.length > 1) {
                measure2 = measures.getItemAt(1) as AnalysisItem;
            }
        }
        var hierarchies:ArrayCollection = findItems(fields, AnalysisItemTypes.HIERARCHY);
        if (hierarchies.length > 0) {
            hierarchy = hierarchies.getItemAt(0) as AnalysisItem;
        }
    }

    override public function showDrilldown(analysisItem:AnalysisItem):Boolean {
        var hierarchy:AnalysisHierarchyItem = analysisItem as AnalysisHierarchyItem;
        var index:int = hierarchy.hierarchyLevels.getItemIndex(hierarchy.hierarchyLevel);
        return (index < (hierarchy.hierarchyLevels.length - 2));
    }

    override public function showRollup(analysisItem:AnalysisItem):Boolean {
        var hierarchy:AnalysisHierarchyItem = analysisItem as AnalysisHierarchyItem;
        var index:int = hierarchy.hierarchyLevels.getItemIndex(hierarchy.hierarchyLevel);
        return index > 0;
    }

    override public function drill(analysisItem:AnalysisItem, data:Object):Event {
        var hierarchyItem:AnalysisHierarchyItem = analysisItem as AnalysisHierarchyItem;
        var index:int = hierarchyItem.hierarchyLevels.getItemIndex(hierarchyItem.hierarchyLevel) + 1;
        var hierarchyBase:AnalysisItem = hierarchyItem.hierarchyLevels.getItemAt(index).analysisItem;
        if (index < (hierarchyItem.hierarchyLevels.length - 1)) {
            var dataField:String = hierarchyBase.qualifiedName();
            var dataString:String = data[dataField];
            var filterRawData:FilterRawData = new FilterRawData();
            filterRawData.addPair(hierarchyBase, dataString);
            hierarchyItem.hierarchyLevel = hierarchyItem.hierarchyLevels.getItemAt(index) as HierarchyLevel;
            return new HierarchyDrilldownEvent(HierarchyDrilldownEvent.DRILLDOWN, filterRawData,
                    hierarchyItem, index);
        }
        return null;
    }

    override public function getValue(analysisItem:AnalysisItem, data:Object):Object {
        var hierarchyItem:AnalysisHierarchyItem = analysisItem as AnalysisHierarchyItem;
        var index:int = hierarchyItem.hierarchyLevels.getItemIndex(hierarchyItem.hierarchyLevel) + 1;
        var hierarchyBase:AnalysisItem = hierarchyItem.hierarchyLevels.getItemAt(index).analysisItem;
        return data[hierarchyBase.qualifiedName()];
    }

    override public function getCoreAnalysisItem(analysisItem:AnalysisItem):AnalysisItem {
        var hierarchyItem:AnalysisHierarchyItem = analysisItem as AnalysisHierarchyItem;
        var index:int = hierarchyItem.hierarchyLevels.getItemIndex(hierarchyItem.hierarchyLevel) + 1;
        return hierarchyItem.hierarchyLevels.getItemAt(index).analysisItem;
    }
}
}