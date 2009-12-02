package com.easyinsight.analysis.timeline {
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.AnalysisItemWrapper;

import flash.events.Event;

import mx.binding.utils.BindingUtils;
import mx.containers.Box;
import mx.controls.AdvancedDataGrid;
import mx.controls.DataGrid;
import mx.controls.Label;
import mx.core.IUIComponent;
import mx.core.UIComponent;
import mx.events.DragEvent;
import mx.managers.DragManager;

public class SequenceBox extends Box {

    private var _sequence:Sequence;

    private var _labelText:String = "Drop a Field Here";

    public function SequenceBox() {
        super();
        this.addEventListener(DragEvent.DRAG_ENTER, dragEnterHandler);
        this.addEventListener(DragEvent.DRAG_DROP, dragDropHandler);
        this.addEventListener(DragEvent.DRAG_OVER, dragOverHandler);
        this.addEventListener(DragEvent.DRAG_EXIT, dragExitHandler);
        this.setStyle("borderStyle", "solid");
        this.setStyle("borderThickness", 1);
        setStyle("verticalAlign", "middle");
        setStyle("backgroundColor", 0xFFFFFF);
    }


    [Bindable(event="labelTextChanged")]
    public function get labelText():String {
        return _labelText;
    }

    public function set labelText(value:String):void {
        if (_labelText == value) return;
        _labelText = value;
        dispatchEvent(new Event("labelTextChanged"));
    }


    protected override function createChildren():void {
        super.createChildren();
        var label:Label = new Label();
        BindingUtils.bindProperty(label, "text", this, "labelText");
        addChild(label);
    }

    [Bindable(event="sequenceChanged")]
    public function get sequence():Sequence {
        return _sequence;
    }

    public function set sequence(value:Sequence):void {
        if (_sequence == value) return;
        _sequence = value;
        if (_sequence != null) {
            labelText = sequence.analysisItem.display;
        } else {
            labelText = "Drop a Field Here";
        }
        dispatchEvent(new Event("sequenceChanged"));
    }

    protected function dragOverHandler(event:DragEvent):void {
        DragManager.showFeedback(DragManager.MOVE);
    }

    protected function dragExitHandler(event:DragEvent):void {
        setStyle("borderColor", 0xB7BABC);
    }

    private function createSequence(item:AnalysisItem):void {
        if (item.hasType(AnalysisItemTypes.DATE)) {
            var dateSequence:DateSequence = new DateSequence();
            dateSequence.analysisItem = item;
            this.sequence = dateSequence;
        }
    }

    private function dragDropHandler(event:DragEvent):void {
        var analysisItem:AnalysisItem;
        if (event.dragInitiator is DataGrid) {
            var initialList:DataGrid = event.dragInitiator as DataGrid;
            var newAnalysisItem:AnalysisItemWrapper = initialList.selectedItem as AnalysisItemWrapper;
            analysisItem = newAnalysisItem.analysisItem;
        } else if (event.dragInitiator is AdvancedDataGrid) {
            var analysisItemLabel:AdvancedDataGrid = event.dragInitiator as AdvancedDataGrid;
            newAnalysisItem = analysisItemLabel.selectedItem as AnalysisItemWrapper;
            analysisItem = newAnalysisItem.analysisItem;
        }
        createSequence(analysisItem);
        labelText = analysisItem.display;
    }

    private function dragEnterHandler(event:DragEvent):void {
        var analysisItem:AnalysisItem = null;
        var okay:Boolean = true;
        if (event.dragInitiator is DataGrid) {
            var initialList:DataGrid = event.dragInitiator as DataGrid;
            var newAnalysisItem:AnalysisItemWrapper = initialList.selectedItem as AnalysisItemWrapper;
            analysisItem = newAnalysisItem.analysisItem;
        } else if (event.dragInitiator is AdvancedDataGrid) {
            var analysisItemLabel:AdvancedDataGrid = event.dragInitiator as AdvancedDataGrid;
            newAnalysisItem = analysisItemLabel.selectedItem as AnalysisItemWrapper;
            if (newAnalysisItem.isAnalysisItem()) {
                analysisItem = newAnalysisItem.analysisItem;
            } else {
                okay = false;
            }
        }
        if (okay) {
            setStyle("borderColor", "green");
            DragManager.acceptDragDrop(event.currentTarget as IUIComponent);
        }
    }
}
}