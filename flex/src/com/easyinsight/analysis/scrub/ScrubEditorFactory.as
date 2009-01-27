package com.easyinsight.analysis.scrub
{
	import mx.collections.ArrayCollection;
	import mx.core.IFactory;

	public class ScrubEditorFactory implements IFactory
	{
		private var fields:ArrayCollection;
		private var feedID:int;
		
		public function ScrubEditorFactory(fields:ArrayCollection, feedID:int)
		{
			this.fields = fields;
			this.feedID = feedID;
		}

		public function newInstance():* {
			return new ScrubControls(fields, feedID);
		}
		
	}
}