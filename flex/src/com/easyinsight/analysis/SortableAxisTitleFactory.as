package com.easyinsight.analysis
{
	import mx.core.IFactory;

	public class SortableAxisTitleFactory implements IFactory
	{
		private var _analysisItem:AnalysisItem;
		private var direction:Boolean;
		
		public function SortableAxisTitleFactory(analysisItem:AnalysisItem, direction:Boolean)
		{
			this._analysisItem = analysisItem;
			this.direction = direction;
		}

		public function newInstance():*
		{
			return new SortableAxisTitle(_analysisItem, direction);
		}
		
		public function set analysisItem(analysisItem:AnalysisItem):void {
			this._analysisItem = analysisItem;
		}
	}
}