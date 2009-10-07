package com.easyinsight.skin {
import com.easyinsight.analysis.AnalysisItem;
import mx.core.IFactory;
public class CustomChartRendererFactory implements IFactory {

    private var _selectedFill:Class;
    private var _rolloverFill:Class;
    private var _analysisItem:AnalysisItem;

    public function CustomChartRendererFactory(selectedFill:Class, rolloverFill:Class, analysisItem:AnalysisItem) {
        _selectedFill = selectedFill;
        _rolloverFill = rolloverFill;
        _analysisItem = analysisItem;
    }

    public function newInstance():* {
        var renderer:CustomDropShadowRenderer = new CustomDropShadowRenderer();
        //renderer.rolloverFill = _rolloverFill;
        renderer.selectedFill = _selectedFill;
        renderer.analysisItem = _analysisItem;
        return renderer;
    }
}
}