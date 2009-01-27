package com.easyinsight.listing
{
	import com.easyinsight.customupload.CommercialUploadPolicy;
	import com.easyinsight.customupload.UploadPolicy;
	
	import mx.controls.Label;

	public class PriceRenderer extends Label
	{
		private var _data:DataFeedDescriptor;
		
		public function PriceRenderer()
		{
			super();
		}
		
		override public function set data(value:Object):void {			
			var descriptor:DataFeedDescriptor = value as DataFeedDescriptor;
			_data = descriptor;
			switch (_data.policy.getPolicyType()) {
				case UploadPolicy.PUBLIC:
				case UploadPolicy.PRIVATE:
					this.text = "Free";
					break;
				case UploadPolicy.COMMERCIAL:
					var policy:CommercialUploadPolicy = _data.policy as CommercialUploadPolicy;
					this.text = "$" + policy.price.cost + "/month";
					break;
			}	
		}
		
		override public function get data():Object {
			return _data;
		}
	}
}