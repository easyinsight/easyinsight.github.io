package com.easyinsight.filtering
{
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.skin.ImageConstants;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.controls.Button;
import mx.controls.CheckBox;
import mx.controls.HSlider;
import mx.controls.Label;
import mx.controls.Text;
import mx.events.SliderEvent;
import mx.formatters.Formatter;
import mx.managers.PopUpManager;
import mx.states.AddChild;
import mx.states.RemoveChild;
import mx.states.State;

public class SliderMeasureFilter extends HBox implements IFilter
{
    private var hslider:HSlider;
    private var _filterDefinition:FilterRangeDefinition;
    private var analysisItem:AnalysisItem;
    private var lowValue:int;
    private var highValue:int;

    private var _lowValueString:String;
    private var _highValueString:String;

    private var lowInput:Label;
    private var highInput:Label;

    private var _analysisItems:ArrayCollection;

    public function SliderMeasureFilter(feedID:int, analysisItem:AnalysisItem) {
        super();
        this.analysisItem = analysisItem;
    }

    private var _loadingFromReport:Boolean = false;


    public function set loadingFromReport(value:Boolean):void {
        _loadingFromReport = value;
    }

    [Bindable(event="lowValueStringChanged")]
    public function get lowValueString():String {
        return _lowValueString;
    }

    public function set lowValueString(value:String):void {
        if (_lowValueString == value) return;
        _lowValueString = value;
        dispatchEvent(new Event("lowValueStringChanged"));
    }

    [Bindable(event="highValueStringChanged")]
    public function get highValueString():String {
        return _highValueString;
    }

    public function set highValueString(value:String):void {
        if (_highValueString == value) return;
        _highValueString = value;
        dispatchEvent(new Event("highValueStringChanged"));
    }

    public function set analysisItems(analysisItems:ArrayCollection):void {
        _analysisItems = analysisItems;
    }

    public function edit(event:MouseEvent):void {
        var window:GeneralFilterEditSettings = new GeneralFilterEditSettings();
        window.detailClass = MeasureFilterEditor;
        window.addEventListener(FilterEditEvent.FILTER_EDIT, onFilterEdit, false, 0, true);
        window.analysisItems = _analysisItems;
        window.filterDefinition = _filterDefinition;
        PopUpManager.addPopUp(window, this, true);
        window.x = 50;
        window.y = 50;
    }

    private function onFilterEdit(event:FilterEditEvent):void {
        var measureFilter:FilterRangeDefinition = event.filterDefinition as FilterRangeDefinition;
        if (measureFilter.startValueDefined) {
            lowValueString = String(measureFilter.startValue);
        } else {
            lowValueString = "";
        }
        if (measureFilter.endValueDefined) {
            highValueString = String(measureFilter.endValue);
        } else {
            highValueString = "";
        }
        if (measureFilter.startValueDefined || measureFilter.endValueDefined) {
            currentState = "Configured";
        } else {
            currentState = "";
        }
        dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, event.filterDefinition, event.previousFilterDefinition, this));
    }

    private function onChange(event:Event):void {
        var checkbox:CheckBox = event.currentTarget as CheckBox;
        _filterDefinition.enabled = checkbox.selected;
        dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, _filterDefinition, null, this));
    }


    override protected function createChildren():void {
        super.createChildren();
        if (lowInput == null) {


            var checkbox:CheckBox = new CheckBox();
            checkbox.selected = _filterDefinition == null ? true : _filterDefinition.enabled;
            checkbox.toolTip = "Click to disable this filter.";
            checkbox.addEventListener(Event.CHANGE, onChange);
            addChild(checkbox);

            if (_filterEditable) {
                var haveDataState:State = new State();
                haveDataState.name = "Configured";
                var defaultBox:HBox = new HBox();
                var removeOp:RemoveChild = new RemoveChild();
                removeOp.target = defaultBox;
                var addChildOp:AddChild = new AddChild();
                haveDataState.overrides = [ removeOp, addChildOp ];
                var box:HBox = new HBox();
                addChildOp.target = box;

                lowInput = new Label();
                BindingUtils.bindProperty(lowInput, "text", this, "lowValueString");
                box.addChild(lowInput);

                var between:Text = new Text();
                between.text = " < " + analysisItem.display + " < ";
                box.addChild(between);

                highInput = new Label();
                BindingUtils.bindProperty(highInput, "text", this, "highValueString");
                box.addChild(highInput);

                var editButton:Button = new Button();
                editButton.addEventListener(MouseEvent.CLICK, edit);
                editButton.setStyle("icon", ImageConstants.EDIT_ICON);
                editButton.toolTip = "Edit";
                box.addChild(editButton);
                var deleteButton:Button = new Button();
                deleteButton.addEventListener(MouseEvent.CLICK, deleteSelf);
                deleteButton.setStyle("icon", ImageConstants.DELETE_ICON);
                deleteButton.toolTip = "Delete";
                box.addChild(deleteButton);


                this.states = [ haveDataState ];


                var editLabel:Label = new Label();
                editLabel.setStyle("fontSize", 10);
                editLabel.text = "Click Edit to Configure";
                defaultBox.addChild(editLabel);
                var editDefault:Button = new Button();
                editDefault.addEventListener(MouseEvent.CLICK, edit);
                editDefault.setStyle("icon", ImageConstants.EDIT_ICON);
                editDefault.toolTip = "Edit";
                defaultBox.addChild(editDefault);
                var deleteDefault:Button = new Button();
                deleteDefault.addEventListener(MouseEvent.CLICK, deleteSelf);
                deleteDefault.setStyle("icon", ImageConstants.DELETE_ICON);
                deleteDefault.toolTip = "Delete";
                defaultBox.addChild(deleteDefault);

                addChild(defaultBox);
            } else {
                setStyle("verticalAlign", "middle");
                var initLabel:Label = new Label();
                initLabel.text = FilterDefinition.getLabel(_filterDefinition, analysisItem);
                addChild(initLabel);
                var f:Formatter = _filterDefinition.field.getFormatter();
                if (_filterDefinition.startValueDefined) {
                    var leftLabel:Label = new Label();
                    leftLabel.text = f.format(_filterDefinition.startValue);
                    addChild(leftLabel);
                }
                var slider:HSlider;
                if (_filterDefinition.startValueDefined && _filterDefinition.endValueDefined) {
                    slider = createDoubleSlider();
                } else if (_filterDefinition.startValueDefined) {
                    slider = createMinSlider();
                } else if (_filterDefinition.endValueDefined) {
                    slider = createMaxSlider();
                }
                if (slider != null) {
                    addChild(slider);
                }
                if (_filterDefinition.endValueDefined) {
                    var rightLabel:Label = new Label();
                    rightLabel.text = f.format(_filterDefinition.endValue);
                    addChild(rightLabel);    
                }
            }
        }


        if (_filterDefinition == null) {
            _filterDefinition = new FilterRangeDefinition();
            _filterDefinition.startValueDefined = false;
            _filterDefinition.endValueDefined = false;
            _filterDefinition.field = analysisItem;

        } else {
            if (_filterDefinition.startValueDefined) {
                lowValueString = _filterDefinition.field.getFormatter().format(_filterDefinition.startValue);
            }
            if (_filterDefinition.endValueDefined) {
                highValueString = _filterDefinition.field.getFormatter().format(_filterDefinition.endValue);
            }
            if (_filterEditable) {
                currentState = "Configured";
            }
        }

        if (_loadingFromReport) {
            _loadingFromReport = false;

        } else {
            dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_ADDED, filterDefinition, null, this));
        }
    }

    private function createDoubleSlider():HSlider {
        var slider:HSlider = new HSlider();
        slider.dataTipFormatFunction = dataTipFormatter;
        slider.minimum = _filterDefinition.startValue;
        slider.maximum = _filterDefinition.endValue;
        //slider.labels = [ _filterDefinition.startValue, _filterDefinition.endValue ];
        slider.showDataTip = true;
        slider.thumbCount = 2;
        slider.liveDragging = false;
        slider.values = [ _filterDefinition.startValue, _filterDefinition.endValue ];
        slider.addEventListener(SliderEvent.THUMB_RELEASE, onRelease);
        slider.setStyle("bottom", 0);
        slider.setStyle("dataTipPlacement", "top");
        return slider;
    }

    private function createMinSlider():HSlider {
        var slider:HSlider = new HSlider();
        slider.dataTipFormatFunction = dataTipFormatter;
        slider.minimum = _filterDefinition.startValue;
        slider.maximum = _filterDefinition.endValue;
        //slider.labels = [ _filterDefinition.startValue, _filterDefinition.endValue ];
        slider.showDataTip = true;
        slider.thumbCount = 1;
        slider.liveDragging = false;
        slider.value = _filterDefinition.startValue;
        slider.addEventListener(SliderEvent.THUMB_RELEASE, onRelease);
        slider.setStyle("bottom", 0);
        slider.setStyle("dataTipPlacement", "top");
        return slider;
    }

    private function createMaxSlider():HSlider {
        var slider:HSlider = new HSlider();
        slider.dataTipFormatFunction = dataTipFormatter;
        slider.minimum = _filterDefinition.startValue;
        slider.maximum = _filterDefinition.endValue;
        //slider.labels = [ _filterDefinition.startValue, _filterDefinition.endValue ];
        slider.showDataTip = true;
        slider.thumbCount = 1;
        slider.liveDragging = false;
        slider.value = _filterDefinition.endValue;
        slider.addEventListener(SliderEvent.THUMB_RELEASE, onRelease);
        slider.setStyle("bottom", 0);
        slider.setStyle("dataTipPlacement", "top");
        return slider;
    }

    private function dataTipFormatter(value:Number):String {
        var f:Formatter = _filterDefinition.field.getFormatter();
        return f.format(value);

    }

    private function onRelease(event:SliderEvent):void {
        var slider:HSlider = event.currentTarget as HSlider;
        if (_filterDefinition.startValueDefined && _filterDefinition.endValueDefined) {
            _filterDefinition.currentStartValue = slider.values[0];
            _filterDefinition.currentStartValueDefined = true;
            _filterDefinition.currentEndValue = slider.values[1];
            _filterDefinition.currentEndValueDefined = true;
        } else if (_filterDefinition.startValueDefined) {
            _filterDefinition.currentStartValue = slider.value;
            _filterDefinition.currentStartValueDefined = true;
        } else if (_filterDefinition.endValueDefined) {
            _filterDefinition.currentEndValue = slider.value;
            _filterDefinition.currentEndValueDefined = true;
        }

        dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, _filterDefinition, null, this));
    }

    public function set filterDefinition(filterDefinition:FilterDefinition):void {
        this._filterDefinition = filterDefinition as FilterRangeDefinition;
    }

    private var _filterEditable:Boolean = true;

    public function set filterEditable(editable:Boolean):void {
        _filterEditable = editable;
    }

    public function get filterDefinition():FilterDefinition {
        return this._filterDefinition;
    }

    private function deleteSelf(event:MouseEvent):void {
        dispatchEvent(new FilterDeletionEvent(this));
    }
}
}