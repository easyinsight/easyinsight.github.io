package com.easyinsight.util
{
	import mx.core.IFactory;

	public class GraphFeedNodeFactory implements IFactory
	{
		public function GraphFeedNodeFactory()
		{
		}

		public function newInstance():*
		{
			return new GraphFeedNode();
		}
		
	}
}