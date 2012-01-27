/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/30/11
 * Time: 2:05 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import com.easyinsight.report.ReportNavigationEvent;
import com.easyinsight.solutions.InsightDescriptor;

import flash.net.URLRequest;
import flash.net.navigateToURL;

import mx.charts.chartClasses.ChartBase;

import mx.collections.ArrayCollection;
import mx.core.UIComponent;

public class ClickUtil {
    
    private static var utils:ArrayCollection = new ArrayCollection();
    
    public function ClickUtil(defaultLink:Link,  data:Object, analysisItem:AnalysisItem, parent:UIComponent, altKey:String, report:AnalysisDefinition) {
        this.defaultLink = defaultLink;
        this.data = data;
        this.analysisItem = analysisItem;
        this.parent = parent;
        this.altKey = altKey;
        this.report = report;
    }
    
    private var defaultLink:Link;
    private var data:Object;
    private var analysisItem:AnalysisItem;
    private var parent:UIComponent;
    private var altKey:String;
    private var report:AnalysisDefinition;
    
    public function argh():void {
        if (defaultLink != null) {
            if (defaultLink is URLLink) {
                var urlLink:URLLink = defaultLink as URLLink;
                var key:String = altKey != null ? altKey : "";
                var url:String = data[key + urlLink.label + "_link"];
                navigateToURL(new URLRequest(url), "_blank");
            } else if (defaultLink is DrillThrough) {
                utils.addItem(this);
                var drillThrough:DrillThrough = defaultLink as DrillThrough;
                var executor:DrillThroughExecutor = new DrillThroughExecutor(drillThrough, data, analysisItem, report);
                executor.addEventListener(DrillThroughEvent.DRILL_THROUGH, onDrill);
                executor.send();
            }
        }
    }

    public static function doStuff(defaultLink:Link,  data:Object, analysisItem:AnalysisItem, parent:UIComponent, report:AnalysisDefinition,
                                   altKey:String = null, chart:ChartBase = null):void {
        var c:ClickUtil = new ClickUtil(defaultLink, data,  analysisItem, parent, altKey, report);
        c.argh();
        if (chart != null) {
            chart.clearSelection();
        }
    }

    private function onDrill(event:DrillThroughEvent):void {
        utils.removeItemAt(utils.getItemIndex(this));
        if (event.drillThrough.miniWindow) {
            parent.dispatchEvent(new ReportWindowEvent(event.drillThroughResponse.descriptor.id, 0, 0, event.drillThroughResponse.filters,
                    InsightDescriptor(event.drillThroughResponse.descriptor).dataFeedID,
                    InsightDescriptor(event.drillThroughResponse.descriptor).reportType));
        } else {
            parent.dispatchEvent(new ReportNavigationEvent(ReportNavigationEvent.TO_REPORT, event.drillThroughResponse.descriptor,
                    event.drillThroughResponse.filters));
        }
    }
}
}
