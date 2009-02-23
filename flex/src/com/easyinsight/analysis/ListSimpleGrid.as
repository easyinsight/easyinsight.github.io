package com.easyinsight.analysis {
import com.easyinsight.dashboard.IView;
import com.easyinsight.framework.DataService;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.collections.Sort;
import mx.controls.AdvancedDataGrid;
import mx.controls.advancedDataGridClasses.AdvancedDataGridColumn;
import mx.events.FlexEvent;

public class ListSimpleGrid extends AdvancedDataGrid implements IView {

    private var _listDefinition:ListDefinition;
    private var dataService:DataService;

    private var _myDataSet:ArrayCollection;

    public function ListSimpleGrid() {
        super();
        BindingUtils.bindProperty(this, "dataProvider", this, "myDataSet");
        this.percentHeight = 100;
        this.percentWidth = 100; 
    }

    [Bindable]
    public function get myDataSet():ArrayCollection {
        return _myDataSet;
    }

    public function set myDataSet(val:ArrayCollection):void {
        _myDataSet = val;
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }

    public function get listDefinition():ListDefinition {
        return _listDefinition;
    }

    public function set listDefinition(val:ListDefinition):void {
        _listDefinition = val;
    }

    protected override function commitProperties():void {
        super.commitProperties();
        if (dataService == null) {
            dataService = new DataService();
            dataService.dataFeedID = _listDefinition.dataFeedID;
            retrieveData();
        }
    }

    private function retrieveData():void {
        dataService.getListData(listDefinition, this, processListResults);
    }

    private function rowNumber(item:Object, iCol:int):String {
        var index:int = dataProvider.getItemIndex(item) + 1;
        return String(index);
    }

    private function processListResults(listRowData:ListDataResults):void {
        var sort:Sort = null;
        if (myDataSet != null) {
             sort = myDataSet.sort;
        }
        myDataSet = new ArrayCollection();
        var headers:ArrayCollection = new ArrayCollection(listRowData.headers as Array);
        var rowData:ArrayCollection = new ArrayCollection(listRowData.rows as Array);
        var clientProcessorMap:Object = new Object();
        for each (var analysisItem:AnalysisItem in headers) {
            clientProcessorMap[analysisItem.qualifiedName()] = analysisItem.createClientRenderer();
        }
        for (var j:int = 0; j < rowData.length; j++) {
            var dataRowObject:Object = rowData.getItemAt(j);
            var rows:ArrayCollection = new ArrayCollection(dataRowObject.values as Array);
            var element:Object = new Object();
            for (var i:int = 0; i < headers.length; i++) {
                var cell:Value = rows.getItemAt(i) as Value;
                var header:AnalysisItem = headers.getItemAt(i) as AnalysisItem;
                if (cell != null) {
                    var value:Object = cell.getValue();
                    var key:String = header.qualifiedName();
                    element[key] = value;
                    clientProcessorMap[key].addValue(value);
                }
            }
            myDataSet.addItem(element);
        }
        if (sort != null) {
            myDataSet.sort = sort;
            myDataSet.refresh();            
        }
        var columns:Array = new Array();
        var initColumn:int = 0;

        if (listDefinition.showLineNumbers) {
            var lineNumberColumn:AdvancedDataGridColumn = new AdvancedDataGridColumn();
            lineNumberColumn.width = 30;
            lineNumberColumn.sortable = false;
            lineNumberColumn.labelFunction = rowNumber;
            columns[0] = lineNumberColumn;
            initColumn++;
        }
        var newColumns:int = 0;
        for each (var newHeader:AnalysisItem in listDefinition.columns) {
            var exists:Boolean = false;
            var qualifiedName:String = newHeader.qualifiedName();
            for each (var existingColumn:AdvancedDataGridColumn in this.columns) {
                if (existingColumn.dataField == qualifiedName) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                newColumns++;
            }
        }
        var gridWidth:int = this.width;
        var modifier:int = gridWidth / listDefinition.columns.length / listDefinition.columns.length * newColumns;
        var widthMap:Object = new Object();
        for each (var column:AdvancedDataGridColumn in this.columns) {
            widthMap[column.dataField] = new String(column.width - modifier);
        }
        for (var k:int = 0; k < listDefinition.columns.length; k++) {
            var myHeader:AnalysisItem = listDefinition.columns.getItemAt(k) as AnalysisItem;
            columnMap[myHeader.qualifiedName()] = myHeader;
            var columnHeaderObject:AdvancedDataGridColumn = new AdvancedDataGridColumn();
            columnHeaderObject.headerText = myHeader.display;
            //columnHeaderObject.headerText = "";
            var keyString:String = myHeader.qualifiedName();
            var existingHeightString:String = widthMap[keyString] as String;
            if (existingHeightString != null) {
                //columnHeaderObject.width = int(existingHeightString);
            }
            columnHeaderObject.dataField = keyString;
            columnHeaderObject.headerRenderer = new ListViewHeaderRendererFactory(myHeader.display);
            columnHeaderObject.itemRenderer = new AnalysisCellRendererFactory(keyString, myHeader,
                    clientProcessorMap[keyString]);
            columns[k + initColumn] = columnHeaderObject;
        }
        this.columns = columns;
    }

    public function refreshData():void {
        retrieveData();
    }
}
}