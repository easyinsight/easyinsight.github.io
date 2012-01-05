/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 8/1/11
 * Time: 10:50 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.skin.ImageConstants;

import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Button;

public class AnalysisItemFilterControls extends HBox {



    private var deleteButton:Button;

    private var field:AnalysisItem;

    public function AnalysisItemFilterControls() {
        setStyle("horizontalAlign", "center");
        this.percentWidth = 100;
        deleteButton = new Button();
        deleteButton.setStyle("icon", ImageConstants.DELETE_ICON);
        deleteButton.toolTip = "Remove Field...";
        deleteButton.addEventListener(MouseEvent.CLICK, onClick);
    }

    private function onClick(event:MouseEvent):void {
        dispatchEvent(new FieldFilterEvent(FieldFilterEvent.REMOVE_FIELD, field));
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(deleteButton);
    }

    override public function set data(val:Object):void {
        field = val as AnalysisItem;
    }

    override public function get data():Object {
        return field;
    }
}
}
