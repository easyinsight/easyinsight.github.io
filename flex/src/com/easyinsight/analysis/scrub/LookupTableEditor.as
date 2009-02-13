package com.easyinsight.analysis.scrub
{
	import flash.events.Event;
	
import mx.binding.utils.BindingUtils;
import mx.containers.HBox;
	import mx.controls.TextInput;
import mx.events.FlexEvent;

	public class LookupTableEditor extends TextInput
	{
		//private var textInput:TextInput;
        private var _lookupText:String;
		private var lookupTablePair:LookupTablePair;		
		
		public function LookupTableEditor()
		{
			super();
			//textInput = new TextInput();
			addEventListener(Event.CHANGE, onChange);
			this.setStyle("percentWidth", 100);
			setStyle("percentWidth", 100);
            //textInput.tabEnabled = true;
			this.setStyle("paddingLeft", 5);
			this.setStyle("paddingRight", 5);
            BindingUtils.bindProperty(this, "text", this, "lookupText");
		}

        [Bindable]
        public function get lookupText():String {
            return _lookupText;
        }

        public function set lookupText(val:String):void {
            _lookupText = val;
        }
		
		private function onChange(event:Event):void {
			lookupTablePair.replaceValue = text;
		}
		
		[Bindable]
		override public function set data(value:Object):void {
			lookupTablePair = value as LookupTablePair;
			this.text = lookupTablePair.replaceValue;
            //this.tabIndex = lookupTablePair.getTabIndex();
		}
		
		override public function get data():Object {
			return lookupTablePair;
		}
	}
}