package com.easyinsight.analysis.scrub
{
	import flash.events.Event;
	
	import mx.containers.HBox;
	import mx.controls.TextInput;

	public class LookupTableEditor extends HBox
	{
		private var textInput:TextInput;
		private var lookupTablePair:LookupTablePair;		
		
		public function LookupTableEditor()
		{
			super();
			textInput = new TextInput();
			textInput.addEventListener(Event.CHANGE, onChange);
			this.setStyle("percentWidth", 100);
			textInput.setStyle("percentWidth", 100);
			this.setStyle("paddingLeft", 5);
			this.setStyle("paddingRight", 5);
			addChild(textInput);			
		}
		
		override protected function createChildren():void {
			super.createChildren();
			if (textInput == null) {
				
			}
			
		}
		
		private function onChange(event:Event):void {
			lookupTablePair.replaceValue = textInput.text;
		}
		
		[Bindable]
		override public function set data(value:Object):void {
			lookupTablePair = value as LookupTablePair;
			textInput.text = lookupTablePair.replaceValue;	
		}
		
		override public function get data():Object {
			return lookupTablePair;
		}
	}
}