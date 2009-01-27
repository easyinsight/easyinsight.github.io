package com.easyinsight.help
{
	import mx.core.IFactory;

	public class ScreencastListRendererFactory implements IFactory
	{
		public function ScreencastListRendererFactory()
		{
		}

		public function newInstance():*
		{
			return new ScreencastListRenderer();
		}
		
	}
}