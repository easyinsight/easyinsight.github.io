package com.easyinsight.filtering
{
	import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.formatter.FormattingConfiguration;

import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.containers.VBox;
import mx.controls.CheckBox;
import mx.controls.ComboBox;
import mx.controls.Text;
	import mx.controls.TextInput;

	public class MeasureFilterEditor extends VBox implements IFilterDetailEditor
	{
		private var lowInput:TextInput;
		private var highInput:TextInput;
		private var _filterDefinition:FilterRangeDefinition;
		
		public function MeasureFilterEditor()
		{
			super();
		}
		
		public function set filterDefinition(filterDefinition:FilterDefinition):void {
			this._filterDefinition = filterDefinition as FilterRangeDefinition;	
		}

        private var _fields:ArrayCollection;

    public function set fields(value:ArrayCollection):void {
        _fields = value;
    }

        private var lowDropdown:ComboBox;
        private var highDropdown:ComboBox;

        private var lowOperator:ComboBox;
        private var highOperator:ComboBox;

        private var showSliderCheckbox:CheckBox;

		override protected function createChildren():void {
			super.createChildren();
			var analysisItem:AnalysisItem = _filterDefinition.field;
            var measure:AnalysisMeasure = analysisItem as AnalysisMeasure;

            var hbox:HBox = new HBox();
			lowInput = new TextInput();
			hbox.addChild(lowInput);

            if (measure.formattingType == FormattingConfiguration.MILLISECONDS) {
                lowDropdown = new ComboBox();
                lowDropdown.dataProvider = new ArrayCollection([ "Days", "Hours"]);
                addChild(lowDropdown);
            }

            var operators:ArrayCollection = new ArrayCollection([{label: "<", data: FilterRangeDefinition.LESS_THAN}, {label: "<=", data: FilterRangeDefinition.LESS_THAN_EQUAL_TO}]);
            lowOperator = new ComboBox();
            lowOperator.dataProvider = operators;
            highOperator = new ComboBox();
            highOperator.dataProvider = operators;
            if(_filterDefinition.lowerOperator == FilterRangeDefinition.LESS_THAN_EQUAL_TO) {
                lowOperator.selectedIndex = 1;
            }
            if(_filterDefinition.upperOperator == FilterRangeDefinition.LESS_THAN_EQUAL_TO) {
                highOperator.selectedIndex = 1;
            }

            hbox.addChild(lowOperator);
			var between:Text = new Text();
			between.text = analysisItem.display;
            hbox.addChild(between);

            hbox.addChild(highOperator);
			
			highInput = new TextInput();
            hbox.addChild(highInput);

            if (measure.formattingType == FormattingConfiguration.MILLISECONDS) {
                highDropdown = new ComboBox();
                highDropdown.dataProvider = new ArrayCollection([ "Days", "Hours"]);
                hbox.addChild(highDropdown);
            }
			
			if (_filterDefinition.startValueDefined) {
                if (measure.formattingType == FormattingConfiguration.MILLISECONDS) {
                    var hours:int = _filterDefinition.startValue / 60 / 60 / 1000;
                    if (hours >= 24) {
                        lowInput.text = String(hours / 24);
                    } else {
                        lowInput.text = String(hours);
                        lowDropdown.selectedItem = "Hours";
                    }
                } else {
				    lowInput.text = String(_filterDefinition.startValue);
                }
			} else {
				lowInput.text = "";
			}
			if (_filterDefinition.endValueDefined) {
                if (measure.formattingType == FormattingConfiguration.MILLISECONDS) {
                    var endHours:int = _filterDefinition.endValue / 60 / 60 / 1000;
                    if (endHours >= 24) {
                        highInput.text = String(endHours / 24);
                    } else {
                        highInput.text = String(endHours);
                        highDropdown.selectedItem = "Hours";
                    }
                } else {
				    highInput.text = String(_filterDefinition.endValue);
                }
			} else {
				highInput.text = "";
			}
            addChild(hbox);
            var labelBox:HBox = new HBox();
            showSliderCheckbox = new CheckBox();
            showSliderCheckbox.label = "Show Slider";
            showSliderCheckbox.selected = _filterDefinition.showSlider;
            labelBox.addChild(showSliderCheckbox);
            addChild(labelBox);
		}
		
		public function makeUpdates():FilterDefinition {
			if (lowInput.text != "") {
                var lowValue:Number;
                if (lowDropdown) {
                    var lowNum:Number = Number(lowInput.text);
                    if (lowDropdown.selectedItem == "Days") {
                        lowValue = lowNum * 60 * 60 * 1000 * 24;
                    } else if (lowDropdown.selectedItem == "Hours") {
                        lowValue = lowNum * 60 * 60 * 1000;
                    }
                } else {
                    lowValue = Number(lowInput.text);
                }
                if (!isNaN(lowValue) && isFinite(lowValue)) {
                    _filterDefinition.startValue = lowValue;
                    _filterDefinition.startValueDefined = true;
                } else {
                    _filterDefinition.startValueDefined = false;
                    _filterDefinition.startValue = 0;
                }
			} else {
				_filterDefinition.startValueDefined = false;
				_filterDefinition.startValue = 0;
			}
			if (highInput.text != "") {

                var highValue:Number;
                if (lowDropdown) {
                    var highNum:Number = Number(highInput.text);
                    if (lowDropdown.selectedItem == "Days") {
                        highValue = highNum * 60 * 60 * 1000 * 24;
                    } else if (lowDropdown.selectedItem == "Hours") {
                        highValue = highNum * 60 * 60 * 1000;
                    }
                } else {
                    highValue = Number(highInput.text);
                }
                if (!isNaN(highValue) && isFinite(highValue)) {
                    _filterDefinition.endValueDefined = true;
                    _filterDefinition.endValue = highValue;
                } else {
                    _filterDefinition.endValueDefined = false;
                    _filterDefinition.endValue = 0;
                }
			} else {
				_filterDefinition.endValueDefined = false;
				_filterDefinition.endValue = 0;
			}

            _filterDefinition.upperOperator = highOperator.selectedItem.data;
            _filterDefinition.lowerOperator = lowOperator.selectedItem.data;

            _filterDefinition.showSlider = showSliderCheckbox.selected;
			return _filterDefinition;
		}

        public function set feedID(feedID:int):void {
        }
    }
}