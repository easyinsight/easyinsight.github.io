package com.easyinsight.customupload
{
	import mx.core.IFactory;

	public class UpdateFeedButtonFactory implements IFactory
	{
		public function UpdateFeedButtonFactory()
		{
		}

		public function newInstance()
		{
			return new UpdateFeedButton();
		}
		
	}
}