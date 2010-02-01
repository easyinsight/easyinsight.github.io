package com.easyinsight.scorecard {
import com.easyinsight.kpi.KPI;

import mx.binding.utils.BindingUtils;
import mx.controls.Button;
import mx.controls.DataGrid;
import mx.core.IUIComponent;
import mx.events.DragEvent;
import mx.managers.DragManager;

public class ScorecardTab extends Button {

    private var _selectedTab:Boolean;
    private var _scorecard:Scorecard;

    public function ScorecardTab() {
        super();
        addEventListener(DragEvent.DRAG_ENTER, dragEnterHandler);
        addEventListener(DragEvent.DRAG_DROP, dragDropHandler);
        addEventListener(DragEvent.DRAG_OVER, dragOverHandler);
        addEventListener(DragEvent.DRAG_EXIT, dragExitHandler);
        setStyle("fontSize", 16);
    }

    public function set scorecard(value:Scorecard):void {
        _scorecard = value;
        if (_scorecard != null) {
            BindingUtils.bindProperty(this, "label", _scorecard, "name");
        }
        invalidateProperties();
    }

    protected override function commitProperties():void {
        super.commitProperties();
    }


    public function get scorecard():Scorecard {
        return _scorecard;
    }

    public function set selectedTab(value:Boolean):void {
        _selectedTab = value;
        if (_selectedTab) {
            styleName = "blueButton";
        } else {
            styleName = "";
        }
    }

    public function get selectedTab():Boolean {
        return _selectedTab;
    }

    private function dragEnterHandler(event:DragEvent):void {
        var obj:Object;
        if (event.dragInitiator is DataGrid) {
            var dataGrid:DataGrid = DataGrid(event.dragInitiator);
            obj = dataGrid.selectedItem;
        }
        if (obj is KPI) {
            DragManager.acceptDragDrop(event.currentTarget as IUIComponent);
        }
    }

    private function dragOverHandler(event:DragEvent):void {

    }

    private function dragDropHandler(event:DragEvent):void {
         var obj:Object;
        if (event.dragInitiator is DataGrid) {
            var dataGrid:DataGrid = DataGrid(event.dragInitiator);
            obj = dataGrid.selectedItem;
        }
        if (obj is KPI) {            
            var kpi:KPI = obj as KPI;
            dispatchEvent(new MoveKPIEvent(kpi, _scorecard));
        }
    }

    private function dragExitHandler(event:DragEvent):void {
        
    }
}
}