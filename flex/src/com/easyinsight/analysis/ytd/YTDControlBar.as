/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/2/11
 * Time: 6:02 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.ytd {
import com.easyinsight.analysis.AnalysisChangedEvent;
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemUpdateEvent;
import com.easyinsight.analysis.CustomChangeEvent;
import com.easyinsight.analysis.DataServiceEvent;
import com.easyinsight.analysis.IReportControlBar;
import com.easyinsight.analysis.ListDropArea;
import com.easyinsight.analysis.ListDropAreaGrouping;
import com.easyinsight.analysis.ReportControlBar;
import com.easyinsight.analysis.ReportDataEvent;

import mx.collections.ArrayCollection;

import mx.controls.Label;

public class YTDControlBar extends ReportControlBar implements IReportControlBar {

    private var listViewGrouping:ListDropAreaGrouping;
    private var columnGrouping:ListDropAreaGrouping;
    private var listDefinition:YTDDefinition;

    [Embed(source="../../../../../assets/table_edit.png")]
    public var tableEditIcon:Class;

    [Embed(source="../../../../../assets/find.png")]
    public var findIcon:Class;

    public function YTDControlBar() {
        super();
        listViewGrouping = new ListDropAreaGrouping();
        listViewGrouping.unlimited = true;
        listViewGrouping.dropAreaType = ListDropArea;
        listViewGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        columnGrouping = new ListDropAreaGrouping();
        columnGrouping.unlimited = false;
        columnGrouping.maxElements = 1;
        columnGrouping.dropAreaType = ListDropArea;
        columnGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        setStyle("verticalAlign", "middle");
    }

    override protected function createChildren():void {
        super.createChildren();
        var columnGroupingLabel:Label = new Label();
        columnGroupingLabel.text = "Column:";
        columnGroupingLabel.setStyle("fontSize", 14);
        addChild(columnGroupingLabel);
        addDropAreaGrouping(columnGrouping);
        var measureLabel:Label = new Label();
        measureLabel.text = "Measures:";
        measureLabel.setStyle("fontSize", 14);
        addChild(measureLabel);
        addDropAreaGrouping(listViewGrouping);
        listViewGrouping.report = listDefinition;

        var columns:ArrayCollection = listDefinition.measures;
        if (columns != null) {
            for (var i:int = 0; i < columns.length; i++) {
                var column:AnalysisItem = columns.getItemAt(i) as AnalysisItem;
                listViewGrouping.addAnalysisItem(column);
            }
        }
        if (listDefinition.timeDimension != null) {
            columnGrouping.addAnalysisItem(listDefinition.timeDimension);
        }
    }

    public function onDataReceipt(event:DataServiceEvent):void {
    }

    private function requestListData(event:AnalysisItemUpdateEvent):void {
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
    }

    public function set analysisDefinition(analysisDefinition:AnalysisDefinition):void {
        listDefinition = analysisDefinition as YTDDefinition;
    }

    public function isDataValid():Boolean {
        return listViewGrouping.getListColumns().length > 0 && columnGrouping.getListColumns().length > 0;
    }

    public function createAnalysisDefinition():AnalysisDefinition {
        listDefinition.measures = new ArrayCollection(listViewGrouping.getListColumns());
        if (columnGrouping.getListColumns().length == 0) {
            listDefinition.timeDimension = null;
        } else {
            listDefinition.timeDimension = columnGrouping.getListColumns()[0];
        }
        return listDefinition;
    }

    public function addItem(analysisItem:AnalysisItem):void {
        listViewGrouping.addAnalysisItem(analysisItem);
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
        dispatchEvent(new AnalysisChangedEvent(false));
    }

    public function onCustomChangeEvent(event:CustomChangeEvent):void {
    }
}
}
