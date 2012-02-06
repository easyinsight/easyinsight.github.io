package com.easyinsight.filtering
{
	import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.formatter.FormattingConfiguration;

import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.controls.ComboBox;
import mx.controls.Text;
	import mx.controls.TextInput;

	public class MeasureFilterEditor extends HBox implements IFilterDetailEditor
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

		override protected function createChildren():void {
			super.createChildren();
			var analysisItem:AnalysisItem = _filterDefinition.field;
            var measure:AnalysisMeasure = analysisItem as AnalysisMeasure;
            
			lowInput = new TextInput();
			addChild(lowInput);

            if (measure.formattingConfiguration.formattingType == FormattingConfiguration.MILLISECONDS) {
                lowDropdown = new ComboBox();
                lowDropdown.dataProvider = new ArrayCollection([ "Days", "Hours"]);
                addChild(lowDropdown);
            }
			
			var between:Text = new Text();
			between.text = " < " + analysisItem.display + " < ";
			addChild(between);
			
			highInput = new TextInput();
			addChild(highInput);

            if (measure.formattingConfiguration.formattingType == FormattingConfiguration.MILLISECONDS) {
                highDropdown = new ComboBox();
                highDropdown.dataProvider = new ArrayCollection([ "Days", "Hours"]);
                addChild(highDropdown);
            }
			
			if (_filterDefinition.startValueDefined) {
                if (measure.formattingConfiguration.formattingType == FormattingConfiguration.MILLISECONDS) {
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
                if (measure.formattingConfiguration.formattingType == FormattingConfiguration.MILLISECONDS) {
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
		}
		
		public function makeUpdates():FilterDefinition {
			if (lowInput.text != "") {
				_filterDefinition.startValueDefined = true;
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
				_filterDefinition.startValue = lowValue;
			} else {
				_filterDefinition.startValueDefined = false;
				_filterDefinition.startValue = 0;
			}
			if (highInput.text != "") {
				_filterDefinition.endValueDefined = true;
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
				_filterDefinition.endValue = highValue;
			} else {
				_filterDefinition.endValueDefined = false;
				_filterDefinition.endValue = 0;
			}
			return _filterDefinition;
		}

        public function set feedID(feedID:int):void {
        }
    }
}