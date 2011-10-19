/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/18/11
 * Time: 3:06 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.datasources {
import com.easyinsight.util.PopUpUtil;

import flash.events.Event;
import flash.events.EventDispatcher;
import flash.events.MouseEvent;

import mx.core.Application;
import mx.core.UIComponent;

import mx.managers.PopUpManager;

public class DataSourceRefreshExecutor extends EventDispatcher {

    private var dataSource:DataSourceInfo;

    public function DataSourceRefreshExecutor() {
    }

    public function refresh(dataSource:DataSourceInfo):void {
        this.dataSource = dataSource;
        if (dataSource.scheduled) {
            forceRefresh();
        } else {
            var setupWindow:DataSourceRefreshSetupWindow = new DataSourceRefreshSetupWindow();
            setupWindow.dataSourceInfo = dataSource;
            setupWindow.addEventListener("done", forceRefresh, false, 0, true);
            PopUpManager.addPopUp(setupWindow, UIComponent(Application.application), true);
            PopUpUtil.centerPopUp(setupWindow);
        }
    }

    private function forceRefresh(event:Event = null):void {
        var dsRefreshWindow:DataSourceRefreshWindow = new DataSourceRefreshWindow();
        dsRefreshWindow.dataSourceID = dataSource.dataSourceID;
        dsRefreshWindow.addEventListener(DataSourceRefreshEvent.DATA_SOURCE_REFRESH, onRefresh, false, 0, true);
        PopUpManager.addPopUp(dsRefreshWindow, UIComponent(Application.application), true);
        PopUpUtil.centerPopUp(dsRefreshWindow);
    }

    private function onRefresh(event:DataSourceRefreshEvent):void {
        /*updateString(event.newDateTime);
        stage.removeEventListener(MouseEvent.MOUSE_DOWN,  onMouseDown);
        PopUpManager.removePopUp(this);*/
        dispatchEvent(event);
    }
}
}
