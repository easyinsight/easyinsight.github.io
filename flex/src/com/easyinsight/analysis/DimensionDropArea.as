package com.easyinsight.analysis
{
	
	public class DimensionDropArea extends DropArea
	{
		public function DimensionDropArea()
		{
			super();
		}
		
		override public function getDropAreaType():String {
			return "Dimension";
		}
		
		override protected function getNoDataLabel():String {
			return "Drag Grouping Here";
		}


        override public function accept(analysisItem:AnalysisItem):Boolean {
            return !analysisItem.hasType(AnalysisItemTypes.MEASURE);
        }

        override public function recommend(analysisItem:AnalysisItem):Boolean {
            return accept(analysisItem);
        }
    }
}