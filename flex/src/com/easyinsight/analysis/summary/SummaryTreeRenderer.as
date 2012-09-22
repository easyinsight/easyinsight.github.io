/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/5/12
 * Time: 2:13 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.summary {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisHierarchyItem;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.HierarchyLevel;

import flash.display.DisplayObject;

import mx.controls.advancedDataGridClasses.AdvancedDataGridGroupItemRenderer;
import mx.controls.advancedDataGridClasses.AdvancedDataGridListData;
import mx.core.IUITextField;

public class SummaryTreeRenderer extends AdvancedDataGridGroupItemRenderer {

    private var _analysisItem:AnalysisItem;
    private var _report:AnalysisDefinition;

    public function SummaryTreeRenderer() {
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
        SummaryTreeTextRenderer(label).analysisItem = levelItem;
        SummaryTreeTextRenderer(label).report = report;
        SummaryTreeTextRenderer(label).data = data;
    }


    override public function set data(value:Object):void {
        super.data = value;
        invalidateProperties();
    }

    override protected function createLabel(childIndex:int):void {
        if (!label)
        {
            label = IUITextField(new SummaryTreeTextRenderer());
            //label.styleName = this;

            if (childIndex == -1)
                addChild(DisplayObject(label));
            else
                addChildAt(DisplayObject(label), childIndex);
        }
    }
}
}