package com.easyinsight.analysis.options
{
	import analysis.AnalysisDateDimension;
	import analysis.AnalysisItem;
	import analysis.AnalysisItemTypes;
	
	import mx.collections.ArrayCollection;
	import mx.controls.ComboBox;
	import mx.events.DropdownEvent;
	
	public class DateOption extends AnalysisItemOption
	{
		private var _dateType:int = AnalysisItemTypes.YEAR_LEVEL;
		private var onChangeObject:Object;
		private var onChangeFunction:Function;
		
		public function DateOption()
		{
			super();
		}
		
		public function get dateType():int {
			return _dateType;
		}
		
		public function set dateType(dateType:int):void {
			this._dateType = dateType;
		}
		
		override protected function getLabelValue():String {
			return "Date";
		}
		
		override public function hasCustomContent():Boolean {
			return true;
		}
		
		override public function createCustomContent(object:Object, caller:Function):Array {
			var dateComboBox:ComboBox = new ComboBox();
			dateComboBox.dataProvider = new ArrayCollection([
				{ label:"Year", data:AnalysisItemTypes.YEAR_LEVEL }, { label:"Month", data:AnalysisItemTypes.MONTH_LEVEL },
				{ label:"Day", data:AnalysisItemTypes.DAY_LEVEL } 
			]);
			this.onChangeFunction = caller;
			this.onChangeObject = object;
			dateComboBox.addEventListener(DropdownEvent.CLOSE, dateTypeChanged);
			return [ dateComboBox ];
		}
		
		private function dateTypeChanged(event:DropdownEvent):void {
			trace("date type changed...");
			var dateType:int = event.currentTarget.selectedItem.data;			
			this.dateType = dateType;
			this.onChangeFunction.call(this.onChangeObject);
		}

	    override public function createAnalysisItem(key:String):AnalysisItem {
	        var analysisDimension:AnalysisDateDimension = new AnalysisDateDimension();
	        analysisDimension.key = key;
	        analysisDimension.dateLevel = dateType;
	        analysisDimension.group = true;
	        return analysisDimension;
	    }
	}
}