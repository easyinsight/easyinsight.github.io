/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/1/11
 * Time: 2:08 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.reportviews {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.EmbeddedDataResults;
import com.easyinsight.analysis.Value;
import com.easyinsight.dashboard.DashboardReport;
import com.easyinsight.framework.InsightRequestMetadata;

import mx.collections.ArrayCollection;
import mx.core.UIComponent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

import spark.components.Group;

public class DashboardReportMobileComponent extends Group {

    public var dashboardReport:DashboardReport;

    private var dataService:RemoteObject;

    public function DashboardReportMobileComponent() {
        dataService = new RemoteObject();
        dataService.destination = "data";
        dataService.endpoint = "https://www.easy-insight.com/app/messagebroker/amfsecure";
        dataService.getEmbeddedResults.addEventListener(ResultEvent.RESULT, gotResults);
        this.percentHeight = 100;
        this.percentWidth = 100;
    }

    private var reportView:IReportView;

    private var _preserveValues:Boolean;

    override protected function createChildren():void {
        super.createChildren();
        createReportDisplay(dashboardReport.report.reportType);
    }

    private function createReportDisplay(type:int):void {
        if (type == AnalysisDefinition.LIST) {
            reportView = new ListReportView();
        } else if (type == AnalysisDefinition.PIE || type == AnalysisDefinition.PIE3D) {
            reportView = new PieChartView();
        } else if (type == AnalysisDefinition.COLUMN || type == AnalysisDefinition.COLUMN3D) {
            reportView = new ColumnChartView();
        } else if (type == AnalysisDefinition.BAR || type == AnalysisDefinition.BAR3D) {
            reportView = new BarChartView();
        } else if (type == AnalysisDefinition.PLOT) {
            reportView = new PlotChartView();
        } else if (type == AnalysisDefinition.LINE) {
            reportView = new LineChartView();
        } else if (type == AnalysisDefinition.AREA) {
            reportView = new AreaChartView();
        } else if (type == AnalysisDefinition.BUBBLE) {
            reportView = new BubbleChartView();
        } else if (type == AnalysisDefinition.TREE) {

        } else {

        }
        _preserveValues = reportView.preserveValues();
        var component:UIComponent = reportView as UIComponent;
        component.percentHeight = 100;
        component.percentWidth = 100;
        addElement(component);
        var requestMetadata:InsightRequestMetadata = new InsightRequestMetadata();
        requestMetadata.utcOffset = new Date().getTimezoneOffset();

        /*var filters:ArrayCollection = null;
        if (metadata.report != null) {
            trace("using existing filters of size = " + metadata.report.filterDefinitions.length);

            filters = metadata.report.filterDefinitions;
        }*/
        dataService.getEmbeddedResults.send(dashboardReport.report.id, dashboardReport.report.dataFeedID, null,
                requestMetadata, new ArrayCollection());
        // mx.core.FlexGlobals.topLevelApplication.viewMenuOpen=true;
    }

        private function gotResults(event:ResultEvent):void {
            trace("got data again");
            var results:EmbeddedDataResults = dataService.getEmbeddedResults.lastResult as EmbeddedDataResults;
            /*var metadata:ReportMetadata = data as ReportMetadata;
            if (metadata.report == null) {
                metadata.report = results.definition;
            }*/
            reportView.renderReport(translateResults(results), results.definition);
            //busy = false;
        }

        private function translateResults(listData:EmbeddedDataResults):ArrayCollection {
            var headers:ArrayCollection = new ArrayCollection(listData.headers);
            var rows:ArrayCollection = new ArrayCollection(listData.rows);
            var data:ArrayCollection = new ArrayCollection();
            for (var i:int = 0; i < rows.length; i++) {
                var row:Object = rows.getItemAt(i);
                var values:Array = row.values as Array;
                var endObject:Object = new Object();
                for (var j:int = 0; j < headers.length; j++) {
                    var headerDimension:AnalysisItem = headers[j] as AnalysisItem;
                    var value:Value = values[j];
                    var key:String = headerDimension.qualifiedName();
                    if (_preserveValues) {
                        endObject[key] = value;
                    } else {
                        endObject[key] = value.getValue();
                    }

                    if (value.links != null) {
                        for (var linkKey:String in value.links) {
                            endObject[linkKey + "_link"] = value.links[linkKey];
                        }
                    }
                }
                data.addItem(endObject);
            }
            return data;
        }
}
}
