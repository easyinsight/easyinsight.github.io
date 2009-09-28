package com.easyinsight.analysis {
import mx.collections.ArrayCollection;

public interface IAnalysisItemEditor {
    function addEventListener(type:String, listener:Function, useCapture:Boolean=false, priority:int=0, useWeakReference:Boolean=false):void;
    function removeEventListener(type:String, listener:Function, useCapture:Boolean = false):void;
    function set analysisItem(analysisItem:AnalysisItem):void;
    function set analysisItems(analysisItems:ArrayCollection):void;
    function save():AnalysisItem;
}
}