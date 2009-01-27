package com.easyinsight.help
{
	public class Screencast
	{
		private var _name:String;
		private var _source:String;
		 
		public function Screencast(name:String, source:String)
		{
			this._name = name;
			this._source = source;
		}

		public function get name():String {
			return _name;
		}
		
		public function get source():String {
			return _source;
		}
	}
}