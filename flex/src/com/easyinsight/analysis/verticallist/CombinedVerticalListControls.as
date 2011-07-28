/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/21/11
 * Time: 11:12 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.verticallist {
import com.easyinsight.analysis.AnalysisDefinition;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Button;

public class CombinedVerticalListControls extends HBox {

    [Embed(source="../../../../../assets/navigate_cross.png")]
    private var deleteIcon:Class;

    private var deleteButton:Button;

    public function CombinedVerticalListControls() {
        deleteButton = new Button();
        deleteButton.setStyle("icon", deleteIcon);
        deleteButton.addEventListener(MouseEvent.CLICK, onClick);
        deleteButton.toolTip = "Remove Report...";
        percentWidth = 100;
        setStyle("horizontalAlign", "center");
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(deleteButton);
    }

    private var report:AnalysisDefinition;

    private function onClick(event:MouseEvent):void {
        dispatchEvent(new CombinedVerticalReportEvent(report));
    }

    override public function set data(val:Object):void {
        this.report = val as AnalysisDefinition;
    }

    override public function get data():Object {
        return this.report;
    }
}
}
