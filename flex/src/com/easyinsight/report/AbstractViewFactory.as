package com.easyinsight.report {
import com.easyinsight.analysis.EmbeddedDataServiceEvent;

import mx.collections.ArrayCollection;
import mx.containers.VBox;

public class AbstractViewFactory extends VBox{

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

    public function gotData(event:EmbeddedDataServiceEvent):void {
        
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

    public function retrieveData(allSources:Boolean = false):void {

    }
}
}