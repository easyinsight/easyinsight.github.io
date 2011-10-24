/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/18/11
 * Time: 12:57 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.report {
import com.easyinsight.analysis.IRetrievable;
import com.easyinsight.datasources.DataSourceDisplay;
import com.easyinsight.datasources.DataSourceInfo;
import com.easyinsight.framework.User;

import flash.events.MouseEvent;
import flash.geom.Point;

import mx.controls.Button;
import mx.managers.PopUpManager;

public class RefreshButton extends Button {

    private var _dataSource:DataSourceInfo;
    private var _viewFactory:IRetrievable;

    public function RefreshButton() {
        addEventListener(MouseEvent.CLICK, onClick);
    }

    public function get dataSource():DataSourceInfo {
        return _dataSource;
    }

    public function set dataSource(value:DataSourceInfo):void {
        _dataSource = value;
    }

    public function set viewFactory(value:IRetrievable):void {
        _viewFactory = value;
    }

    private function onClick(event:MouseEvent):void {
        if (User.getInstance() != null && (_dataSource.type == DataSourceInfo.STORED_PULL || _dataSource.type == DataSourceInfo.COMPOSITE_PULL)) {
            var window:DataSourceDisplay = new DataSourceDisplay();
            var p:Point = new Point(this.x, this.y);
            var g:Point = parent.localToGlobal(p);
            window.x = g.x + 20;
            window.y = g.y + 20;
            window.dataSource = _dataSource;
            window.dataView = _viewFactory;
            PopUpManager.addPopUp(window, this);
        } else {
            _viewFactory.refresh();
        }
    }
}
}
