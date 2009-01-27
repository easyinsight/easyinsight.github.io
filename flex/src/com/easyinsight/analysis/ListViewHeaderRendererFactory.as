package com.easyinsight.analysis
{
	import mx.core.IFactory;

	public class ListViewHeaderRendererFactory implements IFactory
	{
		private var headerText:String;
		
		public function ListViewHeaderRendererFactory(headerText:String)
		{
			this.headerText = headerText;
		}

		public function newInstance():*
		{
			return new ListViewHeaderRenderer(headerText);
		}
		
	}
}