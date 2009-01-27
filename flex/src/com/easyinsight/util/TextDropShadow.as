package com.easyinsight.util
{
	import flash.display.Bitmap;
	import flash.display.BitmapData;
	import flash.text.TextField;
	
	import mx.core.UIComponent;

	public class TextDropShadow extends UIComponent
	{
		private var _useShadow:Boolean = false;
		private var _shadowHolder:Bitmap;
		private var _bitmapData:BitmapData;
		private var _textField:TextField;	
		
		public function TextDropShadow()
		{
			super();
			_shadowHolder = new Bitmap();
			addChild(_shadowHolder);
			_shadowHolder.x = 5;
			_shadowHolder.y = 5;
			_textField = new TextField();
			addChild(_textField);
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			if (_useShadow) {
				_bitmapData = new BitmapData(_textField.width, _textField.height), true);
				_bitmapData.draw(_textField);
				_shadowHolder.bitmapData = _bitmapData;
				_shadowHolder.alpha = 0.7;
			}
		}
		
		public function set useShadow(value:Boolean):void {
			_useShadow = value;
		}
		
		public function get useShadow():Boolean {
			return _useShadow;
		}
	}
}