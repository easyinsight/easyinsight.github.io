/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 6/13/11
 * Time: 4:49 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.dashboard {
import com.easyinsight.analysis.AnalysisDefinition;

import mx.core.UIComponent;

public class DashboardElementFactory {

    private static var counter:int = 0;

    public function DashboardElementFactory() {
    }

    public static function createEditorUIComponent(element:DashboardElement, dashboardEditorMetadata:DashboardEditorMetadata):UIComponent {
        if (element is DashboardGrid) {
            var gridComp:DashboardGridEditorComponent = new DashboardGridEditorComponent();
            gridComp.dashboardGrid = element as DashboardGrid;
            gridComp.dashboardEditorMetadata = dashboardEditorMetadata;
            return gridComp;
        } else if (element is DashboardStack) {
            var stackComp:DashboardStackEditorComponent = new DashboardStackEditorComponent();
            stackComp.dashboardStack = element as DashboardStack;
            stackComp.dashboardEditorMetadata = dashboardEditorMetadata;
            return stackComp;
        } else if (element is DashboardImage) {
            var imageComp:DashboardImageEditorComponent = new DashboardImageEditorComponent();
            imageComp.image = element as DashboardImage;
            return imageComp;
        } else if (element is DashboardReport) {
            var reportComp:DashboardReportEditorComponent = new DashboardReportEditorComponent();
            reportComp.report = element as DashboardReport;
            return reportComp;
        } else if (element is DashboardScorecard) {
            var comp:DashboardScorecardEditorComponent = new DashboardScorecardEditorComponent();
            comp.scorecard = element as DashboardScorecard;
            return comp;
        }
        return null;
    }

    public static function createViewUIComponent(element:DashboardElement, dashboardEditorMetadata:DashboardEditorMetadata):UIComponent {
        if (element is DashboardGrid) {
            var dashboardGrid:DashboardGrid = element as DashboardGrid;
            var optimizable:Boolean = true;
            for each (var gridItem:DashboardGridItem in dashboardGrid.gridItems) {
                var item:DashboardElement = gridItem.dashboardElement;
                if (!(item is DashboardReport)) {
                    optimizable = false;
                    break;
                }
                var report:DashboardReport = item as DashboardReport;
                if (report.report.reportType != AnalysisDefinition.GAUGE) {
                    optimizable = false;
                    break;
                }
            }
            if (optimizable) {
                var optimizedGridComp:DashboardOptimizedGridViewComponent = new DashboardOptimizedGridViewComponent();
                optimizedGridComp.dashboardGrid = element as DashboardGrid;
                optimizedGridComp.dashboardEditorMetadata = dashboardEditorMetadata;
                return optimizedGridComp;
            } else {
                var gridComp:DashboardGridViewComponent = new DashboardGridViewComponent();
                gridComp.dashboardGrid = element as DashboardGrid;
                gridComp.dashboardEditorMetadata = dashboardEditorMetadata;
                return gridComp;
            }
        } else if (element is DashboardStack) {
            var stackComp:DashboardStackViewComponent = new DashboardStackViewComponent();
            stackComp.dashboardStack = element as DashboardStack;
            stackComp.dashboardEditorMetadata = dashboardEditorMetadata;
            stackComp.elementID = String(counter++);
            return stackComp;
        } else if (element is DashboardImage) {
            var imageComp:DashboardImageViewComponent = new DashboardImageViewComponent();
            imageComp.dashboardImage = element as DashboardImage;
            return imageComp;
        } else if (element is DashboardReport) {
            var reportComp:DashboardReportViewComponent = new DashboardReportViewComponent();
            reportComp.dashboardReport = element as DashboardReport;
            return reportComp;
        } else if (element is DashboardScorecard) {
            var comp:DashboardScorecardViewComponent = new DashboardScorecardViewComponent();
            comp.dashboardScorecard = element as DashboardScorecard;
            return comp;
        }
        return null;
    }
}
}
