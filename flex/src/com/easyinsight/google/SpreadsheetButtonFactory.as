package com.easyinsight.google
{
	import mx.core.IFactory;

	public class SpreadsheetButtonFactory implements IFactory
	{
		public function SpreadsheetButtonFactory()
		{
		}

		public function newInstance():*
		{
			return new SpreadsheetFeedButton();
		}
		
	}
}