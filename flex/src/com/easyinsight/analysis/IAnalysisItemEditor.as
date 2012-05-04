package com.easyinsight.analysis {
import mx.collections.ArrayCollection;

public interface IAnalysisItemEditor {
    function addEventListener(type:String, listener:Function, useCapture:Boolean=false, priority:int=0, useWeakReference:Boolean=false):void;
    function removeEventListener(type:String, listener:Function, useCapture:Boolean = false):void;
    function set analysisItem(analysisItem:AnalysisItem):void;
    function set analysisItems(analysisItems:ArrayCollection):void;
    function set dataSourceID(dataSourceID:int):void;
    function save(dataSourceID:int):void;
    function validate():Boolean;
    function higlight():void;
    function normal():void;
    function set report(analysisDefinition:AnalysisDefinition):void;
}
}