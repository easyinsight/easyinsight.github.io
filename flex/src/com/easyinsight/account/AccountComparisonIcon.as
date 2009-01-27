package com.easyinsight.account
{
	import mx.controls.Image;
	
	public class AccountComparisonIcon
	{
		[Bindable]
        [Embed(source="../../../../assets/check.png")]
        public var listIcon:Class;
        
        private var properties:Object = new Object();
        
        private var _comparisonRowName:String;
		
		public function get comparisonRowName():String {
			return this._comparisonRowName;
		}
        
		public function AccountComparisonIcon(label:String, free:Boolean,
			individual:Boolean, professional:Boolean)
		{
			this._comparisonRowName = label;
			if (free) properties["free"] = listIcon;
			if (individual) properties["individual"] = listIcon;
			if (professional) properties["professional"] = listIcon;
		}
		
		public function getImage(key:String):Class {
			return properties[key]; 									
		}
	}
}