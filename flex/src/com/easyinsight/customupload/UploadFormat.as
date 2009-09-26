package com.easyinsight.customupload
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.userupload.UploadFormat")]
	public class UploadFormat
	{
		public static const FLAT_FILE:int = 1;
		public static const EXCEL:int = 2;
        public static const CSV:int = 3;
		
		public function UploadFormat()
		{
		}

		public function getFormatType():int {
			return 0;
		}                                              
	}
}