package com.easyinsight.filtering
{
	import mx.containers.Tile;
	import mx.core.UIComponent;

	public class ExpansiveTile extends Tile
	{
		public function ExpansiveTile()
		{
			super();
		}
		
		override protected function measure():void {
			super.measure();
			var combinedHeight:int = tileHeight;			
			var factor:int = Math.floor(this.measuredWidth / tileWidth);
			var factorCounter:int = 0;			
			for each (var component:UIComponent in getChildren()) {
				factorCounter++;
				if (factorCounter >= factor) {
				 	combinedHeight += tileWidth;
				 	factorCounter = 0;
			    }				 
			}
			this.measuredHeight = combinedHeight;
		}
	}
}