package com.easyinsight.analysis.conditions
{
	/*
		Flyweight pattern to avoid creating a bunch of extra renderers.
	*/
	public class NoConditionRenderer extends ConditionRenderer
	{
		private static var defaultRenderer:NoConditionRenderer;
		
		public function NoConditionRenderer()
		{
			super();
		}
		
		public static function getDefaultRenderer():ConditionRenderer {
			if (defaultRenderer == null) {
				defaultRenderer = new NoConditionRenderer();
			}
			return defaultRenderer;
		}
	}
}