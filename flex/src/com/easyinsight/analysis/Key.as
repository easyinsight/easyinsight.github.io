package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.core.Key")]
	public class Key
	{
		public var keyID:Number;
		//public var displayName:String;
		
		public function Key()
		{
		}
		
		public function createString():String {
			return null;
		}

        public function internalString():String {
            return null;
        }
				
		/*public function createDisplayName():String {
			if (displayName != null) {
				return displayName;
			}
			return createString();			
		}*/
	}
}