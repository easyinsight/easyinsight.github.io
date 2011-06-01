package com.easyinsight.analysis.list
{
import com.easyinsight.analysis.*;
	public class LimitsEntry
	{
		private var _name:String;
		private var _top:Boolean;
		private var _number:int; 
		
		public function LimitsEntry(name:String = null, top:Boolean = true, number:int = 0)
		{
            this.name = name;
            this.top = top;
            this.number = number;
		}
		
		public function set name(name:String):void {
			this._name = name;
		}
		
		public function set top(top:Boolean):void {
			this._top = top;
		}
		
		public function set number(number:int):void {
			this._number = number;
		}
		
		public function get name():String {
			return this._name;
		}
		
		public function get number():int {
			return this._number;
		}
		
		public function get top():Boolean {
			return this._top;
		}

		public function toLimitsMetadata(limitsMetadata:LimitsMetadata):void {			
			limitsMetadata.number = _number;
			limitsMetadata.top = _top;			
		}
	}
}