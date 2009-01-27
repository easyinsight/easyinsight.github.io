package com.easyinsight.genredata
{
	import mx.core.IFactory;

	public class PopularItemFactory implements IFactory
	{
		private var classString:Class;
		
		public function PopularItemFactory(classString:Class)
		{
			this.classString = classString;
		}

		public function newInstance():*
		{
			return new classString(); 
		}
		
	}
}