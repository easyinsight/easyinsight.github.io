package com.easyinsight.analysis {
import flash.events.Event;
import flash.events.MouseEvent;
import mx.collections.ArrayCollection;
import mx.containers.Box;
import mx.containers.VBox;
import mx.containers.ViewStack;
import mx.controls.ComboBox;
import mx.controls.HorizontalList;
import mx.controls.Image;
import mx.events.ListEvent;
public class ReportTypeSelector extends VBox{

    [Embed(source="../../../../assets/table.png")]
    private var gridIcon:Class;

    [Embed(source="../../../../assets/chart_column.png")]
    private var chartIcon:Class;

    [Embed(source="../../../../assets/earth.png")]
    private var mapIcon:Class;

    [Embed(source="../../../../assets/gauge.png")]
    private var gaugeIcon:Class;

    private var viewStack:ViewStack;

    private var map:Object;

    private var buttonList:HorizontalList;

    private var topTypes:ArrayCollection = new ArrayCollection([ ]);

    public function ReportTypeSelector() {
        super();
    }

    public function assignType(type:int):void {
        for (var i:int = 0; i < topTypes.length; i++) {
            var key:String = topTypes.getItemAt(i) as String;
            var comboBox:ComboBox = map[key];
            var grids:ArrayCollection = ArrayCollection(comboBox.dataProvider);
            for each (var obj:Object in grids) {
                if (obj.type == type) {
                    buttonList.selectedIndex = i;
                    comboBox.selectedItem = obj;
                }
            }
        }
    }

    private function topClick(event:ListEvent):void {
        viewStack.selectedIndex = event.columnIndex;
    }

    override protected function createChildren():void {
        super.createChildren();
        buttonList = new HorizontalList();
        buttonList.addEventListener(ListEvent.ITEM_CLICK, topClick);

        var gridButton:Image = new Image();
        gridButton.source = gridIcon;

        var box:Box = new Box();
        var comboBox:ComboBox = new ComboBox();
        comboBox.dataProvider = new ArrayCollection( [ { label: "List", type: AnalysisDefinition.LIST }, { label: "Crosstab", type: AnalysisDefinition.CROSSTAB } ] );
        comboBox.addEventListener(Event.CHANGE, onSubTypeChoice);
        box.addChild(comboBox);
    }

    private function onSubTypeChoice(event:Event):void {
        var value:Object = event.currentTarget.selectedValue;
        var type:int = value.type;
        var controllerClass:Class = ControllerLookup.controllerForType(type);
        var controller:IReportController = new controllerClass();
        dispatchEvent(new AnalysisStateChangeEvent(controller));
    }
}
}