/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/30/11
 * Time: 2:05 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import com.easyinsight.filtering.FilterValueDefinition;
import com.easyinsight.report.ReportNavigationEvent;
import com.easyinsight.solutions.InsightDescriptor;

import flash.net.URLRequest;
import flash.net.navigateToURL;

import mx.collections.ArrayCollection;
import mx.core.UIComponent;

public class ClickUtil {
    
    private static var utils:ArrayCollection = new ArrayCollection();
    
    public function ClickUtil(defaultLink:Link,  data:Object, analysisItem:AnalysisItem, parent:UIComponent, altKey:String) {
        this.defaultLink = defaultLink;
        this.data = data;
        this.analysisItem = analysisItem;
        this.parent = parent;
        this.altKey = altKey;
    }
    
    private var defaultLink:Link;
    private var data:Object;
    private var analysisItem:AnalysisItem;
    private var parent:UIComponent;
    private var altKey:String;
    
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
                var executor:DrillThroughExecutor = new DrillThroughExecutor(drillThrough);
                executor.addEventListener(DrillThroughEvent.DRILL_THROUGH, onDrill);
                executor.send();
            }
        }
    }

    public static function doStuff(defaultLink:Link,  data:Object, analysisItem:AnalysisItem, parent:UIComponent, altKey:String = null):void {
        var c:ClickUtil = new ClickUtil(defaultLink, data,  analysisItem, parent, altKey);
        c.argh();
    }

    private function onDrill(event:DrillThroughEvent):void {
        utils.removeItemAt(utils.getItemIndex(this));
        var filters:ArrayCollection;
        if (analysisItem.hasType(AnalysisItemTypes.DIMENSION)) {
            var filterDefinition:FilterValueDefinition = new FilterValueDefinition();
            filterDefinition.field = analysisItem;
            filterDefinition.singleValue = true;
            if (altKey == null) {
                filterDefinition.filteredValues = new ArrayCollection([data[analysisItem.qualifiedName()]]);
            } else {
                filterDefinition.filteredValues = new ArrayCollection([altKey]);
            }
            filterDefinition.enabled = true;
            filterDefinition.inclusive = true;
            filters = new ArrayCollection([ filterDefinition ]);
        }
        if (event.drillThrough.miniWindow) {
            parent.dispatchEvent(new ReportWindowEvent(event.report.id, 0, 0, filters, InsightDescriptor(event.report).dataFeedID, InsightDescriptor(event.report).reportType));
        } else {
            parent.dispatchEvent(new ReportNavigationEvent(ReportNavigationEvent.TO_REPORT, event.report, filters));
        }
    }
}
}
