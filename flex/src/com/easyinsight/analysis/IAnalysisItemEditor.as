package com.easyinsight.analysis {
import mx.collections.ArrayCollection;

public interface IAnalysisItemEditor {
    function set analysisItem(analysisItem:AnalysisItem):void;
    function set analysisItems(analysisItems:ArrayCollection):void;
    function save():AnalysisItem;
}
}