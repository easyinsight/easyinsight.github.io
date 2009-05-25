package com.easyinsight.analysis
{
	import mx.controls.ComboBox;
	
	public class HierarchyDropArea extends DropArea
	{
		private var aggregationBox:ComboBox;
		
		public function HierarchyDropArea()
		{
			super();		
		}

        override protected function supportsDrilldown():Boolean {
            return false;
        }
		
		override public function getDropAreaType():String {
			return "Hierarchy";
		}
		
		override public function getItemEditorClass():Class {
			return MeasureItemEditor;
		}
		
		override protected function getNoDataLabel():String {
			return "Drop Hierarchy Here";
		}

        override public function set analysisItem(analysisItem:AnalysisItem):void {

            if (analysisItem != null) {
                if (!analysisItem.hasType(AnalysisItemTypes.HIERARCHY)) {
                }
            }
            
            super.analysisItem = analysisItem;

        }
	}
}