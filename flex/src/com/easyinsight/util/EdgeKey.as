package com.easyinsight.util
{
	import mx.core.UIComponent;
	
	public class EdgeKey
	{
		private var fromComp:UIComponent;
		private var toComp:UIComponent;
		
		public function EdgeKey(fromComp:UIComponent, toComp:UIComponent)
		{
			this.fromComp = fromComp;
			this.toComp = toComp;
		}

	}
}