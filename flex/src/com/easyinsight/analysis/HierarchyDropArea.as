package com.easyinsight.analysis
{
	
	public class HierarchyDropArea extends DropArea
	{
		
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
		
		override protected function getNoDataLabel():String {
			return "Drag Hierarchy Here";
		}

        override public function accept(analysisItem:AnalysisItem):Boolean {
            if (analysisItem == null) {
                return false;
            }
            return analysisItem.hasType(AnalysisItemTypes.HIERARCHY);
        }

        override public function recommend(analysisItem:AnalysisItem):Boolean {
            return accept(analysisItem);
        }

        override public function customEditor():Class {
            return HierarchyWindow;
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