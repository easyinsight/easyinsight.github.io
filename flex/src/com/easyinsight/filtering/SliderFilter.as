package com.easyinsight.filtering
{
	import com.easyinsight.analysis.AnalysisItem;
	
	import mx.containers.HBox;
	import mx.controls.HSlider;
	
	public class SliderFilter extends HBox
	{
		private var slider:HSlider;
		
		public function SliderFilter(analysisItem:AnalysisItem)
		{
		}

		private function emitEvent():void {
			
		}
		
		public function get filterDefinition():FilterDefinition {
			return null;
		}
	}
}