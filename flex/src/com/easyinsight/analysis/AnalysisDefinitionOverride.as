package com.easyinsight.analysis {
import mx.core.UIComponent;
import mx.core.UIComponent;
import mx.states.IOverride;
public class AnalysisDefinitionOverride implements IOverride {

    private var _analysisDefinition:AnalysisDefinition;

    private var _dataAnalysisContainer:DataAnalysisContainer;


    public function set dataAnalysisContainer(val:DataAnalysisContainer):void {
        _dataAnalysisContainer = val;
    }
    public function initialize():void {
    }
    public function apply(parent:UIComponent):void {
    }
    public function remove(parent:UIComponent):void {
    }
}
}