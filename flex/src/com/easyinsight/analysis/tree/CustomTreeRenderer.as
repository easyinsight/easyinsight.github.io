package com.easyinsight.analysis.tree {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisHierarchyItem;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.HierarchyLevel;

import flash.display.DisplayObject;

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
        var levelItem:AnalysisItem = HierarchyLevel(AnalysisHierarchyItem(analysisItem).hierarchyLevels.getItemAt(AdvancedDataGridListData(listData).depth - 1)).analysisItem;
        levelItem.reportFieldExtension = analysisItem.reportFieldExtension;
        CustomTreeTextRenderer(label).analysisItem = levelItem;
        CustomTreeTextRenderer(label).report = report;
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