package com.easyinsight.analysis.charts {
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.ChartDefinition;
import com.easyinsight.analysis.ListDataResults;
import com.easyinsight.analysis.Value;
import com.easyinsight.dashboard.IView;
import com.easyinsight.framework.DataService;
import mx.collections.ArrayCollection;
import mx.controls.Alert;

public class SimpleColumnChart extends ColumnChartAdapter implements IView {

    private var dataService:DataService;
    private var _chartDefinition:ChartDefinition;

    private var graphData:ArrayCollection;

    [Bindable]
    [Embed(source="../../../../../assets/watersmall.jpg")]
    private var waterFill:Class;

    public function SimpleColumnChart() {
        super();
        this.percentHeight = 100;
        this.percentWidth = 100;
        this.standardFill = waterFill;
        this.selectedFill = waterFill;
        this.rolloverFill = waterFill;
    }


    public function get chartDefinition():ChartDefinition {
        return _chartDefinition;
    }

    public function set chartDefinition(val:ChartDefinition):void {
        _chartDefinition = val;
    }

    protected override function commitProperties():void {
        super.commitProperties();
        if (dataService == null) {
            dataService = new DataService();
            dataService.dataFeedID = _chartDefinition.dataFeedID;
            retrieveData();
        }
    }

    private function retrieveData():void {
        dataService.getListData(chartDefinition, this, processListResults);
    }

    private function processListResults(listRowData:ListDataResults):void {
        var headers:ArrayCollection = new ArrayCollection(listRowData.headers);
        var rows:ArrayCollection = new ArrayCollection(listRowData.rows);
        graphData = new ArrayCollection();
        Alert.show("got back " + rows.length + " rows");
        for (var i:int = 0; i < rows.length; i++) {
            var row:Object = rows.getItemAt(i);
            var values:Array = row.values as Array;
            var endObject:Object = new Object();
            for (var j:int = 0; j < headers.length; j++) {
                var headerDimension:AnalysisItem = headers[j];
                var value:Value = values[j];
                endObject[headerDimension.key.createString()] = value.getValue();
            }
            graphData.addItem(endObject);
        }
        dataChange(graphData, chartDefinition.dimensions.toArray(), chartDefinition.measures.toArray());
    }

    public function refreshData():void {
        retrieveData();
    }
}
}