package com.easyinsight.util
{
	import mx.controls.Menu;
	import mx.controls.PopUpMenuButton;
	import mx.core.IUIComponent;
	import mx.core.mx_internal;
	use namespace mx_internal;
	
	public class IconPopUpMenuButton extends PopUpMenuButton
	{
		private var _iconField:String;
		
		public function IconPopUpMenuButton()
		{
			super();
		}
		
		public function set iconField(iconField:String):void {
			this._iconField = iconField;
		}
		
		override mx_internal function getPopUp():IUIComponent {
			var menu:Menu = super.getPopUp() as Menu;
			menu.iconField = this._iconField;
			return menu;
		}
	}
}