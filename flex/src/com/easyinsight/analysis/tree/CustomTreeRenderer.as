package com.easyinsight.analysis.tree {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisHierarchyItem;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.HierarchyLevel;

import flash.display.DisplayObject;
import flash.utils.describeType;

import mx.collections.GroupingCollection;

import mx.controls.AdvancedDataGrid;

import mx.controls.Alert;

import mx.controls.advancedDataGridClasses.AdvancedDataGridGroupItemRenderer;
import mx.controls.advancedDataGridClasses.AdvancedDataGridListData;
import mx.core.IUITextField;

public class CustomTreeRenderer extends AdvancedDataGridGroupItemRenderer {

    private var _analysisItem:AnalysisItem;
    private var _report:AnalysisDefinition;

    public function CustomTreeRenderer() {
        super();
        mouseEnabled = true;
    }

    public function get analysisItem():AnalysisItem {
        return _analysisItem;
    }

    public function set analysisItem(val:AnalysisItem):void {
        _analysisItem = val;
    }

    public function get report():AnalysisDefinition {
        return _report;
    }

    public function set report(value:AnalysisDefinition):void {
        _report = value;
    }

    protected override function commitProperties():void {
        super.commitProperties();
        //Alert.show("depth = " + AdvancedDataGridListData(listData).depth);
        var levelItem:AnalysisItem = HierarchyLevel(AnalysisHierarchyItem(analysisItem).hierarchyLevels.getItemAt(AdvancedDataGridListData(listData).depth - 1)).analysisItem;
        CustomTreeTextRenderer(label).analysisItem = levelItem;
        CustomTreeTextRenderer(label).report = report;

        var grid:AdvancedDataGrid = AdvancedDataGrid(listData.owner);
        /*var gColl:GroupingCollection = grid.dataProvider as GroupingCollection;
        var data:Object = gColl.source.getItemAt(AdvancedDataGridListData(listData).rowIndex);
        Alert.show(data[levelItem.qualifiedName()]);*/
        /*var item:Object = AdvancedDataGridListData(listData).item;
        Alert.show(item[levelItem.qualifiedName()].toString());*/
        CustomTreeTextRenderer(label).data = data;
    }


    override public function set data(value:Object):void {
        super.data = value;
        invalidateProperties();
    }

    override protected function createLabel(childIndex:int):void {
        if (!label)
        {
            label = IUITextField(new CustomTreeTextRenderer());
            //label.styleName = this;

            if (childIndex == -1)
                addChild(DisplayObject(label));
            else
                addChildAt(DisplayObject(label), childIndex);
        }
    }
}
}