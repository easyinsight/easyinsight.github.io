package com.easyinsight.filtering
{
	import com.easyinsight.analysis.AnalysisItem;

import flash.events.Event;
import flash.events.MouseEvent;


import mx.collections.ArrayCollection;
	import mx.containers.HBox;
import mx.controls.CheckBox;
import mx.controls.HSlider;
	import mx.controls.Label;
import mx.events.SliderEvent;
import mx.formatters.NumberFormatter;

public class EmbeddedSliderMeasureFilter extends HBox implements IEmbeddedFilter
	{
		//private var dataService:RemoteObject;
		private var hslider:HSlider;
		private var _filterDefinition:FilterRangeDefinition;
		private var analysisItem:AnalysisItem;
		private var lowValue:int;
		private var highValue:int;
		private var lowField:Label;
		private var highField:Label;

    private var _lowValueString:String;
    private var _highValueString:String;
		
		private var lowInput:Label;
		private var highInput:Label;
		
		private var _analysisItems:ArrayCollection;
		
		public function EmbeddedSliderMeasureFilter(feedID:int, analysisItem:AnalysisItem) {
			super();
			this.analysisItem = analysisItem;
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
		
		private function onFilterEdit(event:FilterEditEvent):void {
            var measureFilter:FilterRangeDefinition = event.filterDefinition as FilterRangeDefinition;
            if (measureFilter.startValueDefined) {
                lowValueString = String(measureFilter.startValue);
            }
            if (measureFilter.endValueDefined) {
                highValueString = String(measureFilter.endValue);
            }
            if (measureFilter.startValueDefined || measureFilter.endValueDefined) {
                currentState = "Configured";
            } else {
                currentState = "";
            }
			dispatchEvent(new EmbeddedFilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, event.filterDefinition, event.previousFilterDefinition, this));
		}

    private function onChange(event:Event):void {
            var checkbox:CheckBox = event.currentTarget as CheckBox;
            _filterDefinition.enabled = checkbox.selected;
            dispatchEvent(new EmbeddedFilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, _filterDefinition, null, this));
        }


    override protected function createChildren():void {
        super.createChildren();
        if (lowInput == null) {


                var checkbox:CheckBox = new CheckBox();
                checkbox.selected = _filterDefinition == null ? true : _filterDefinition.enabled;
                checkbox.toolTip = "Click to disable this filter.";
                checkbox.addEventListener(Event.CHANGE, onChange);
                addChild(checkbox);


                    setStyle("verticalAlign", "middle");
                    var initLabel:Label = new Label();
                    initLabel.text = analysisItem.display + ":";
                    addChild(initLabel);
                    var leftLabel:Label = new Label();
                    var nf:NumberFormatter = new NumberFormatter();
                    leftLabel.text = nf.format(_filterDefinition.startValue);
                    addChild(leftLabel);
                    var slider:HSlider = new HSlider();
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
                    addChild(slider);
                    var rightLabel:Label = new Label();
                    rightLabel.text = nf.format(_filterDefinition.endValue);
                    addChild(rightLabel);
                }
           



                if (_filterDefinition.startValueDefined) {
                    lowValueString = String(_filterDefinition.startValue);
                }
                if (_filterDefinition.endValueDefined) {
                    highValueString = String(_filterDefinition.endValue);
                }
                if (_filterEditable) {
                    currentState = "Configured";
                }
            

			dispatchEvent(new EmbeddedFilterUpdatedEvent(FilterUpdatedEvent.FILTER_ADDED, filterDefinition, null, this));
    }

    private function onRelease(event:SliderEvent):void {
        var slider:HSlider = event.currentTarget as HSlider;
        _filterDefinition.startValue = slider.values[0];
        _filterDefinition.endValue = slider.values[1];
        dispatchEvent(new EmbeddedFilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, _filterDefinition, null, this));
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
			dispatchEvent(new EmbeddedFilterDeletionEvent(this));
		}

        private var _showLabel:Boolean;

        public function set showLabel(show:Boolean):void {
            _showLabel = show;
        }
	}
}