package com.easyinsight.help
{
	public class Screencast
	{
		private var _source:String;
		 
		public function Screencast(source:String)
		{
			this._source = source;
		}
		
		public function get source():String {
			return _source;
		}
	}
}