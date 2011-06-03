/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 6/3/11
 * Time: 10:23 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.report {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.genredata.AnalyzeEvent;
import com.easyinsight.listing.AnalysisDefinitionAnalyzeSource;
import com.easyinsight.solutions.InsightDescriptor;
import com.easyinsight.util.PopUpUtil;

import flash.display.DisplayObject;

import flash.events.ContextMenuEvent;
import flash.events.EventDispatcher;

import flash.ui.ContextMenu;
import flash.ui.ContextMenuItem;

import mx.managers.PopUpManager;

public class AppReportContextMenuFactory extends EventDispatcher implements IReportContextMenuFactory {
    public function AppReportContextMenuFactory() {
    }

    public function createReportContextMenu(insightDescriptor:InsightDescriptor, viewFactory:EmbeddedViewFactory, dObj:DisplayObject):ContextMenu {
        var navigateItem:ContextMenuItem = new ContextMenuItem("View " + insightDescriptor.name);
        navigateItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, function(event:ContextMenuEvent):void {
            dispatchEvent(new AnalyzeEvent(new ReportAnalyzeSource(insightDescriptor)));
        });
        var editItem:ContextMenuItem = new ContextMenuItem("Edit " + insightDescriptor.name);
        editItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, function(event:ContextMenuEvent):void {
            dispatchEvent(new AnalyzeEvent(new AnalysisDefinitionAnalyzeSource(insightDescriptor)));
        });
        var exportItem:ContextMenuItem = new ContextMenuItem("Export " + insightDescriptor.name);
        exportItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, function(event:ContextMenuEvent):void {
            viewFactory.updateExportMetadata();
            var window:ReportExportWindow = new ReportExportWindow();
            window.report = viewFactory.report;
            window.coreView = viewFactory.getChildAt(0);
            PopUpManager.addPopUp(window, dObj, true);
            PopUpUtil.centerPopUp(window);
        });
        var menu:ContextMenu = new ContextMenu();
        menu.hideBuiltInItems();
        menu.customItems = [ navigateItem, editItem, exportItem ];
        return menu;
    }
}
}
