package com.easyinsight.analysis
{
	
	public class DateDropArea extends DropArea
	{
		public function DateDropArea()
		{
			super();
		}
		
		override public function getDropAreaType():String {
			return "Date";
		}
		
		override protected function getNoDataLabel():String {
			return "Drag Date Here";
		}


        override public function accept(analysisItem:AnalysisItem):Boolean {
            return analysisItem.hasType(AnalysisItemTypes.DATE);
        }

        override public function recommend(analysisItem:AnalysisItem):Boolean {
            return accept(analysisItem);
        }
    }
}