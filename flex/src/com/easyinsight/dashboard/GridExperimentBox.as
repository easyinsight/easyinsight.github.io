/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/23/13
 * Time: 1:32 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.dashboard {
import com.easyinsight.util.EISlimWindow;
import com.easyinsight.util.PopUpUtil;

import flash.events.MouseEvent;

import mx.containers.Box;

import mx.containers.Canvas;

import mx.containers.Grid;
import mx.containers.GridItem;
import mx.containers.GridRow;
import mx.controls.Button;
import mx.core.Application;
import mx.events.FlexEvent;
import mx.managers.PopUpManager;

public class GridExperimentBox extends Box {

    public var dashboardGrid:DashboardGrid;
    public var dashboardGridViewComponent:DashboardLabeledGridViewComponent;

    public function GridExperimentBox() {
        setStyle("horizontalAlign", "center");
        setStyle("paddingTop", 5);
    }

    private var grid:Grid;

    override protected function createChildren():void {
        super.createChildren();
        grid = new Grid();
        for (var i:int = 0; i < dashboardGrid.rows; i++) {
            var gridRow:GridRow = new GridRow();
            grid.addChild(gridRow);
            gridRow.setStyle("paddingLeft", 0);
            gridRow.setStyle("paddingRight", 0);
            gridRow.setStyle("paddingTop", 0);
            gridRow.setStyle("paddingBottom", 0);
            for (var j:int = 0; j < dashboardGrid.columns; j++) {
                var e:DashboardGridItem = findItem(i, j);
                var gridItem:GridItem = new GridItem();

                if (e.dashboardElement is DashboardReport) {
                    var button:Button = new Button();
                    button.styleName = "dashboardGrayButton";
                    button.label = DashboardReport(e.dashboardElement).report.name;
                    button.data = dashboardGridViewComponent.findChild(j, i) as IDashboardViewComponent;
                    button.addEventListener(MouseEvent.CLICK, onClick);
                    gridItem.addChild(button);
                }
                gridRow.addChild(gridItem);
            }
        }
        addChild(grid);
    }

    private var selectedComp:DashboardReportViewComponent;

    public function destroy():void {
        if (selectedComp != null) {
            selectedComp.unhighlight();
        }
    }

    private function onClick(event:MouseEvent):void {
        var comp:DashboardReportViewComponent = event.currentTarget.data as DashboardReportViewComponent;
        selectedComp = comp;
        comp.highlight();
        var box:ExperimentBox = new ExperimentBox();
        box.viewFactory = comp.viewFactory;
        box.insightDescriptor = comp.dashboardReport.report;
        box.dashboardReportViewComponent = comp;
        removeChild(grid);
        addChild(box);
    }

    private function findItem(x:int, y:int):DashboardGridItem {
        for each (var e:DashboardGridItem in dashboardGrid.gridItems) {
            if (e.rowIndex == x && e.columnIndex == y) {
                return e;
            }
        }
        return null;
    }
}
}
