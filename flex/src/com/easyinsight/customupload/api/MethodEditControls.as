package com.easyinsight.customupload.api {
import com.easyinsight.analysis.AnalysisItem;
import flash.events.MouseEvent;
import mx.controls.Button;
import mx.containers.HBox;
public class MethodEditControls extends HBox {
    private var deleteButton:Button;
    private var analysisItem:AnalysisItem;

    [Bindable]
    [Embed(source="../../../../../assets/navigate_cross.png")]
    public var closeIcon:Class;

    public function MethodEditControls() {
        super();
        deleteButton = new Button();
        deleteButton.toolTip = "Delete";
        deleteButton.setStyle("icon", closeIcon);
        deleteButton.addEventListener(MouseEvent.CLICK, onDelete);
        setStyle("percentWidth", 100);
        setStyle("horizontalAlign", "center");
        addChild(deleteButton);
    }

    override public function get data():Object {
        return analysisItem;
    }

    override public function set data(object:Object):void {
        this.analysisItem = object as AnalysisItem;
    }

    private function onDelete(event:MouseEvent):void {
        parent.dispatchEvent(new MethodItemEvent(MethodItemEvent.METHOD_ITEM_DELETED, analysisItem));
    }
}
}