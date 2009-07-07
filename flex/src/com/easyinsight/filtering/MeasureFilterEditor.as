package com.easyinsight.filtering
{
	import com.easyinsight.analysis.AnalysisItem;
	
	import mx.containers.HBox;
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
		
		override protected function createChildren():void {
			super.createChildren();
			var analysisItem:AnalysisItem = _filterDefinition.field;
			lowInput = new TextInput();
			addChild(lowInput);
			
			var between:Text = new Text();
			between.text = " < " + analysisItem.display + " < ";
			addChild(between);
			
			highInput = new TextInput();
			addChild(highInput);
			
			if (_filterDefinition.startValueDefined) {
				lowInput.text = String(_filterDefinition.startValue);
			} else {
				lowInput.text = "";
			}
			if (_filterDefinition.endValueDefined) {
				highInput.text = String(_filterDefinition.endValue);
			} else {
				highInput.text = "";
			}
		}
		
		public function makeUpdates():FilterDefinition {
			if (lowInput.text != "") {
				_filterDefinition.startValueDefined = true;
				_filterDefinition.startValue = int(lowInput.text);
			} else {
				_filterDefinition.startValueDefined = false;
				_filterDefinition.startValue = 0;
			}
			if (highInput.text != "") {
				_filterDefinition.endValueDefined = true;
				_filterDefinition.endValue = int(highInput.text);
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