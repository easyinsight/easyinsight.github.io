package com.easyinsight.util {
import flash.text.TextFieldAutoSize;
	import mx.core.mx_internal;
	import mx.controls.TextArea;
;

	public class AutoSizeTextArea extends TextArea
	{
		public function AutoSizeTextArea()
		{
			super();
		}
		override public function set text(value:String):void
		{
			super.text = value ;
			updateSize() ;
		}
		override public function set htmlText(value:String):void
		{
			super.data = value ;
			updateSize() ;
		}
		override protected function commitProperties():void
		{
			super.commitProperties() ;
			updateSize() ;
		}
		protected function updateSize():void
		{
			if(mx_internal::getTextField() != null)
			{
				validateNow() ;
				mx_internal::getTextField().autoSize = TextFieldAutoSize.LEFT ;
				mx_internal::getTextField().validateNow();
				this.height = mx_internal::getTextField().height ;
			}
		}
	}

}