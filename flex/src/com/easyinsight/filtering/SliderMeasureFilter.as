package com.easyinsight.filtering
{
	import com.easyinsight.analysis.AnalysisItem;
	import com.easyinsight.analysis.AnalysisItemResultMetadata;
	import com.easyinsight.analysis.AnalysisMeasureResultMetadata;

import com.easyinsight.framework.CredentialsCache;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
	import mx.containers.HBox;
	import mx.controls.Button;
	import mx.controls.HSlider;
	import mx.controls.Label;
	import mx.controls.Text;
	import mx.managers.PopUpManager;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
import mx.states.AddChild;
import mx.states.RemoveChild;
import mx.states.State;

public class SliderMeasureFilter extends HBox implements IFilter
	{
		private var dataService:RemoteObject;
		private var hslider:HSlider;
		private var _filterDefinition:FilterRangeDefinition;
		private var analysisItem:AnalysisItem;
		private var lowValue:int;
		private var highValue:int;
		private var lowField:Label;
		private var highField:Label;

    private var _lowValueString:String;
    private var _highValueString:String;
		
		private var lowInput:Label;
		private var highInput:Label;
		
		private var _analysisItems:ArrayCollection;
		
		[Embed(source="../../../../assets/navigate_cross.png")]
        public var deleteIcon:Class;
        
        [Bindable]
        [Embed(source="../../../../assets/pencil.png")]
        public var editIcon:Class;
		
		public function SliderMeasureFilter(feedID:int, analysisItem:AnalysisItem) {
			super();
			this.analysisItem = analysisItem;
			dataService = new RemoteObject();
			dataService.destination = "data";
			dataService.getAnalysisItemMetadata.addEventListener(ResultEvent.RESULT, gotMetadata);
			dataService.getAnalysisItemMetadata.send(feedID, analysisItem, CredentialsCache.getCache().createCredentials(), new Date().getTimezoneOffset());
		}


    [Bindable(event="lowValueStringChanged")]
    public function get lowValueString():String {
        return _lowValueString;
    }

    public function set lowValueString(value:String):void {
        if (_lowValueString == value) return;
        _lowValueString = value;
        dispatchEvent(new Event("lowValueStringChanged"));
    }

    [Bindable(event="highValueStringChanged")]
    public function get highValueString():String {
        return _highValueString;
    }

    public function set highValueString(value:String):void {
        if (_highValueString == value) return;
        _highValueString = value;
        dispatchEvent(new Event("highValueStringChanged"));
    }

    public function set analysisItems(analysisItems:ArrayCollection):void {
			_analysisItems = analysisItems;
		}		
		
		private function edit(event:MouseEvent):void {
			var window:GeneralFilterEditSettings = new GeneralFilterEditSettings();
			window.detailClass = MeasureFilterEditor;
			window.addEventListener(FilterEditEvent.FILTER_EDIT, onFilterEdit);
			window.analysisItems = _analysisItems;
			window.filterDefinition = _filterDefinition;
			PopUpManager.addPopUp(window, this, true);
			window.x = 50;
			window.y = 50;
		}		
		
		private function onFilterEdit(event:FilterEditEvent):void {
            var measureFilter:FilterRangeDefinition = event.filterDefinition as FilterRangeDefinition;
            if (measureFilter.startValueDefined) {
                lowValueString = String(measureFilter.startValue);
            }
            if (measureFilter.endValueDefined) {
                highValueString = String(measureFilter.endValue);
            }
            if (measureFilter.startValueDefined || measureFilter.endValueDefined) {
                currentState = "Configured";
            } else {
                currentState = "";
            }
			dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, event.filterDefinition, event.previousFilterDefinition, this));
		}
		
		private function gotMetadata(event:ResultEvent):void {
			var metadata:AnalysisItemResultMetadata = dataService.getAnalysisItemMetadata.lastResult as AnalysisItemResultMetadata;
			var measureMetadata:AnalysisMeasureResultMetadata = metadata as AnalysisMeasureResultMetadata;

            if (lowInput == null) {
                var haveDataState:State = new State();
                haveDataState.name = "Configured";
                var defaultBox:HBox = new HBox();
                var removeOp:RemoveChild = new RemoveChild();
                removeOp.target = defaultBox;
                var addChildOp:AddChild = new AddChild();
                haveDataState.overrides = [ removeOp, addChildOp ];
                var box:HBox = new HBox();
                addChildOp.target = box;

                lowInput = new Label();
                BindingUtils.bindProperty(lowInput, "text", this, "lowValueString");
                box.addChild(lowInput);

                var between:Text = new Text();
                between.text = " < " + analysisItem.display + " < ";
                box.addChild(between);

                highInput = new Label();
                BindingUtils.bindProperty(highInput, "text", this, "highValueString");
			    box.addChild(highInput);

                if (_filterEditable) {
                    var editButton:Button = new Button();
                    editButton.addEventListener(MouseEvent.CLICK, edit);
                    editButton.setStyle("icon", editIcon);
                    editButton.toolTip = "Edit";
                    box.addChild(editButton);
                    var deleteButton:Button = new Button();
                    deleteButton.addEventListener(MouseEvent.CLICK, deleteSelf);
                    deleteButton.setStyle("icon", deleteIcon);
                    deleteButton.toolTip = "Delete";
                    box.addChild(deleteButton);
                }



                this.states = [ haveDataState ];

                if (_filterEditable) {
                    var editLabel:Label = new Label();
                    editLabel.setStyle("fontSize", 10);
                    editLabel.text = "Click Edit to Configure";
                    defaultBox.addChild(editLabel);
                    var editDefault:Button = new Button();
                    editDefault.addEventListener(MouseEvent.CLICK, edit);
                    editDefault.setStyle("icon", editIcon);
                    editDefault.toolTip = "Edit";
                    defaultBox.addChild(editDefault);
                    var deleteDefault:Button = new Button();
                    deleteDefault.addEventListener(MouseEvent.CLICK, deleteSelf);
                    deleteDefault.setStyle("icon", deleteIcon);
                    deleteDefault.toolTip = "Delete";
                    defaultBox.addChild(deleteDefault);
                }
                addChild(defaultBox);
            }

			
			if (_filterDefinition == null) {
				_filterDefinition = new FilterRangeDefinition();
				_filterDefinition.startValueDefined = false;
				_filterDefinition.endValueDefined = false;
				_filterDefinition.field = analysisItem;
					
			} else {
                if (_filterDefinition.startValueDefined) {
                    lowValueString = String(_filterDefinition.startValue);
                }
                if (_filterDefinition.endValueDefined) {
                    highValueString = String(_filterDefinition.endValue);
                }
                currentState = "Configured";
            }
			
			dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_ADDED, filterDefinition, null, this));
		}
		
		public function set filterDefinition(filterDefinition:FilterDefinition):void {
			this._filterDefinition = filterDefinition as FilterRangeDefinition;
		}

        private var _filterEditable:Boolean = true;

        public function set filterEditable(editable:Boolean):void {
            _filterEditable = editable;
        }
		
		public function get filterDefinition():FilterDefinition {
			return this._filterDefinition;
		}
		
		private function deleteSelf(event:MouseEvent):void {
			dispatchEvent(new FilterDeletionEvent(this));
		}

        private var _showLabel:Boolean;

        public function set showLabel(show:Boolean):void {
            _showLabel = show;
        }
	}
}