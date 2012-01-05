/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 2/28/11
 * Time: 11:52 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.feedassembly {
import com.easyinsight.skin.ImageConstants;
import com.easyinsight.solutions.DataSourceDescriptor;
import com.easyinsight.solutions.FeedSelectionEvent;

import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Button;

public class DataSourceControls extends HBox {



    private var deleteButton:Button;

    private var dataSource:DataSourceDescriptor;

    public function DataSourceControls() {
        deleteButton = new Button();
        deleteButton.setStyle("icon", ImageConstants.DELETE_ICON);
        deleteButton.toolTip = "Remove this data source";
        deleteButton.addEventListener(MouseEvent.CLICK, onDelete);
        setStyle("horizontalAlign", "center");
    }

    private function onDelete(event:MouseEvent):void {
        dispatchEvent(new FeedSelectionEvent(FeedSelectionEvent.FEED_DELETED, dataSource));
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(deleteButton);
    }

    override public function set data(val:Object):void {
        dataSource = val as DataSourceDescriptor;
    }

    override public function get data():Object {
        return dataSource;
    }
}
}
