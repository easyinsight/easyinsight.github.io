package com.easyinsight.customupload
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.userupload.FlatFileUploadFormat")]
	public class FlatFileUploadFormat extends UploadFormat
	{
		public var delimiter:String;
		public var escapeSequence:String;
		
		public function FlatFileUploadFormat()
		{
			super();
		}
		
		override public function getFormatType():int {
			return UploadFormat.FLAT_FILE;
		}
	}
}