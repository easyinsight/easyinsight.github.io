/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 1/12/13
 * Time: 11:22 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.administration.feed {
import com.easyinsight.analysis.AnalysisItemEditEvent;
import com.easyinsight.analysis.AnalysisItemWrapper;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.collections.ArrayCollection;

import mx.controls.ComboBox;
import mx.controls.LinkButton;
import mx.controls.listClasses.IListItemRenderer;
import mx.core.UIComponent;
import mx.events.FlexEvent;

public class FieldTypeComboBox extends UIComponent implements IListItemRenderer {

    private var button:LinkButton;

    public function FieldTypeComboBox() {

    }

    override protected function createChildren():void {
        super.createChildren();
        if (button == null) {
            button = new LinkButton();
            button.label = "Edit...";
            button.addEventListener(MouseEvent.CLICK, onClick);
        }
        addChild(button);
    }

    private function onClick(event:MouseEvent):void {
        dispatchEvent(new AnalysisItemEditEvent(AnalysisItemWrapper(dataVal).analysisItem, AnalysisItemWrapper(dataVal), true));
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        if (button != null) {
            button.move(8,0);
            button.setActualSize(52, 20);
        }
    }
    private var dataVal:Object;

    [Bindable("dataChange")]
    public function set data(val:Object):void {
        dataVal = val;
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }

    public function get data():Object {
        return dataVal;
    }
}
}
