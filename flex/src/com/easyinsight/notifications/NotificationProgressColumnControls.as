package com.easyinsight.notifications {
import com.easyinsight.gnip.*;
import com.easyinsight.datasources.GnipFilter;

import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Alert;
import mx.controls.Button;
import mx.controls.Label;
import mx.controls.ProgressBar;
import mx.events.CloseEvent;
import mx.managers.PopUpManager;

public class NotificationProgressColumnControls extends HBox {

    private var progress:ProgressBar;
    private var infoLabel:Label;

    [Bindable("dataChange")]
    [Inspectable(environment="none")]

    override public function get data():Object {
        return dataItem;
    }

    override public function set data(value:Object):void {

        this.dataItem = value as RefreshEventInfo;
        this.removeAllChildren();
        if(dataItem.action == RefreshEventInfo.ADD) {
            progress = new ProgressBar();
            progress.percentWidth = 100;
            progress.indeterminate = true;
            this.addChild(progress);
        }
        else {
            infoLabel = new Label();
            infoLabel.text = dataItem.message;
            this.addChild(infoLabel);
        }
    }

    private var dataItem:RefreshEventInfo;
    

    public function NotificationProgressColumnControls() {
        this.setStyle("horizontalAlign", "center");
        this.percentWidth = 100;
    }

    override protected function createChildren():void {
        super.createChildren();

    }
}
}