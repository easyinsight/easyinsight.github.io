package com.easyinsight.report {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItemOverride;
import com.easyinsight.analysis.EmbeddedDataServiceEvent;
import com.easyinsight.analysis.IRetrievable;
import com.easyinsight.framework.HierarchyOverride;

import mx.collections.ArrayCollection;
import mx.containers.VBox;

public class AbstractViewFactory extends VBox implements IRetrievable {

    private var _reportID:int;
    private var _dataSourceID:int;
    private var _availableFields:ArrayCollection;
    private var _filterDefinitions:ArrayCollection;
    private var _reportType:int;

    public function AbstractViewFactory() {
        super();
        this.percentHeight = 100;
        this.percentWidth = 100;
    }

    public function addOverride(hierarchyOverride:AnalysisItemOverride):void {
        
    }

    public function set prefix(val:String):void {
        
    }

    public function gotData(event:EmbeddedDataServiceEvent):void {
        
    }

    public function set noCache(val:Boolean):void {
        
    }

    public function get reportType():int {
        return _reportType;
    }

    public function set reportType(value:int):void {
        _reportType = value;
    }

    public function set availableFields(val:ArrayCollection):void {
        _availableFields = val;
    }

    public function set reportID(val:int):void {
        _reportID = val;
    }

    public function loadRenderer():void {
        
    }

    public function set dataSourceID(value:int):void {
        _dataSourceID = value;
    }


    public function get reportID():int {
        return _reportID;
    }

    public function get dataSourceID():int {
        return _dataSourceID;
    }

    public function get availableFields():ArrayCollection {
        return _availableFields;
    }

    public function set filterDefinitions(value:ArrayCollection):void {
        _filterDefinitions = value;
    }

    public function get filterDefinitions():ArrayCollection {
        return _filterDefinitions;
    }

    private var _drillthroughFilters:ArrayCollection;

    public function set drillthroughFilters(value:ArrayCollection):void {
        _drillthroughFilters = value;
    }

    public function get drillthroughFilters():ArrayCollection {
        return _drillthroughFilters;
    }

    public function get report():AnalysisDefinition {
        return null;
    }

    public function updateExportMetadata():void {
        
    }

    public function retrieveData(allSources:Boolean = false):void {

    }
}
}