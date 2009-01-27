package com.easyinsight.analysis
{
import com.easyinsight.analysis.options.AnalysisItemOption;
import com.easyinsight.analysis.options.AverageOption;
import com.easyinsight.analysis.options.CountOption;
import com.easyinsight.analysis.options.DateOption;
import com.easyinsight.analysis.options.GroupingOption;
import com.easyinsight.analysis.options.MaxOption;
import com.easyinsight.analysis.options.MinOption;
import com.easyinsight.analysis.options.RangeOption;
import com.easyinsight.analysis.options.SumOption;

import flash.display.DisplayObject;

import mx.collections.ArrayCollection;
import mx.controls.ComboBox;
import mx.events.DropdownEvent;
	
	public class ListDropArea extends DropArea
	{
		[Bindable]
		private var _aggregationType:String = "Sum";
		private var aggregationBox:ComboBox;
		private var previousDisplayObject:DisplayObject;
		[Bindable]
		private var aggregationTypeOptions:ArrayCollection;
		
		private var aggregationTypeMap:Object = new Object();
		
		public function ListDropArea()
		{
			super();
		}
		
		override public function getDropAreaType():String {
			return "List";
		}
		
		override protected function getNoDataLabel():String {
			return "Drop Item Here";
		}
	}
}