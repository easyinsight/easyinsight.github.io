package com.easyinsight.analysis
{
	import com.easyinsight.util.ValueAwareComboBox;
	
	import mx.collections.ArrayCollection;
	import mx.containers.HBox;
	import mx.controls.Label;
	import mx.controls.TextInput;

	public class ResultLimitComboBox extends HBox
	{
		private var comboBox:ValueAwareComboBox;
		private var limitLabel:Label;
		private var customEntryInput:TextInput;
		private var comboBoxOptions:ArrayCollection;
		
		public function ResultLimitComboBox()
		{
			super();
		}
		
		public function set limits(options:ArrayCollection):void {
			this.comboBoxOptions = options;
		}
		
		override protected function createChildren():void {
			if (limitLabel == null) {
				limitLabel = new Label();
				limitLabel.text = "Maximum Results: ";				
			}
			addChild(limitLabel);
			if (comboBox == null) {
				comboBox = new ValueAwareComboBox();
				comboBox.dataProvider = comboBoxOptions;				
			}
			addChild(comboBox);
		} 
	}
}