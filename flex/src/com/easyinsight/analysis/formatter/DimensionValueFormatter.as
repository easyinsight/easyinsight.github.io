package com.easyinsight.analysis.formatter
{
	import mx.formatters.Formatter;

	public class DimensionValueFormatter extends Formatter
	{
		public function DimensionValueFormatter()
		{
			super();
		}
		
		override public function format(value:Object):String {
			return String(value);
		}
	}
}