package com.easyinsight.filtering
{
	import com.easyinsight.analysis.AnalysisDateDimensionResultMetadata;
	import com.easyinsight.analysis.AnalysisItem;
	import com.easyinsight.analysis.AnalysisItemResultMetadata;

import com.easyinsight.framework.CredentialsCache;

import com.easyinsight.util.ProgressAlert;

import flash.events.Event;
import flash.events.MouseEvent;
	import flash.geom.Point;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.containers.Box;
import mx.containers.HBox;
import mx.containers.ViewStack;
import mx.controls.Alert;
import mx.controls.Button;
import mx.controls.CheckBox;
import mx.controls.DateField;
	import mx.controls.HSlider;
import mx.controls.Label;
import mx.events.CalendarLayoutChangeEvent;
	import mx.events.SliderEvent;
import mx.formatters.DateFormatter;
import mx.managers.PopUpManager;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;

	public class SliderDateFilter extends HBox implements IFilter
	{
		private var dataService:RemoteObject;
		private var hslider:HSlider;
		private var lowField:DateField;
		private var highField:DateField;
		private var lowDate:Date;
		private var highDate:Date;
		private var delta:Number;
		private var analysisItem:AnalysisItem;
		private var _filterDefinition:FilterDateRangeDefinition;
		private var _analysisItems:ArrayCollection;

        private var _loadingFromReport:Boolean = false;


    public function set loadingFromReport(value:Boolean):void {
        _loadingFromReport = value;
    }
		
		[Bindable]
        [Embed(source="../../../../assets/navigate_cross.png")]
        public var deleteIcon:Class;
        
        [Bindable]
        [Embed(source="../../../../assets/pencil.png")]
        public var editIcon:Class;
		
		public function SliderDateFilter(feedID:int, analysisItem:AnalysisItem) {
			super();
			this.analysisItem = analysisItem;
			dataService = new RemoteObject();
			dataService.destination = "data";
			dataService.getAnalysisItemMetadata.addEventListener(ResultEvent.RESULT, gotMetadata);
            dataService.getAnalysisItemMetadata.addEventListener(FaultEvent.FAULT, onFault);
			dataService.getAnalysisItemMetadata.send(feedID, analysisItem, CredentialsCache.getCache().createCredentials(), new Date().getTimezoneOffset());
		}

        private function onFault(event:FaultEvent):void {
            Alert.show(event.fault.faultDetail);
        }

        private function dataTipFormatter(value:Number):String {
            var date:Date = new Date(lowDate.valueOf() + delta * (value / 100));
            var df:DateFormatter = new DateFormatter();
            return df.format(date);
            
        }

        private var _leftLabel:String;

        private var _rightLabel:String;

        private var _leftIndex:int;

        private var _rightIndex:int;


        [Bindable(event="leftLabelChanged")]
        public function get leftLabel():String {
            return _leftLabel;
        }

        public function set leftLabel(value:String):void {
            if (_leftLabel == value) return;
            _leftLabel = value;
            dispatchEvent(new Event("leftLabelChanged"));
        }

        [Bindable(event="rightLabelChanged")]
        public function get rightLabel():String {
            return _rightLabel;
        }

        public function set rightLabel(value:String):void {
            if (_rightLabel == value) return;
            _rightLabel = value;
            dispatchEvent(new Event("rightLabelChanged"));
        }

        [Bindable(event="leftIndexChanged")]
        public function get leftIndex():int {
            return _leftIndex;
        }

        public function set leftIndex(value:int):void {
            if (_leftIndex == value) return;
            _leftIndex = value;
            dispatchEvent(new Event("leftIndexChanged"));
        }

        [Bindable(event="rightIndexChanged")]
        public function get rightIndex():int {
            return _rightIndex;
        }

        public function set rightIndex(value:int):void {
            if (_rightIndex == value) return;
            _rightIndex = value;
            dispatchEvent(new Event("rightIndexChanged"));
        }

        private function onChange(event:Event):void {
            var checkbox:CheckBox = event.currentTarget as CheckBox;
            _filterDefinition.enabled = checkbox.selected;
            dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, _filterDefinition, null, this));
        }
		
		private function gotMetadata(event:ResultEvent):void {
			var metadata:AnalysisItemResultMetadata = dataService.getAnalysisItemMetadata.lastResult as AnalysisItemResultMetadata;
			var dateMetadata:AnalysisDateDimensionResultMetadata = metadata as AnalysisDateDimensionResultMetadata;
			this.lowDate = dateMetadata.earliestDate;
			this.highDate = dateMetadata.latestDate;
			this.delta = highDate.valueOf() - lowDate.valueOf();
			hslider = new HSlider();
			hslider.thumbCount = 2;
			hslider.liveDragging = false;
			hslider.minimum = 0;
            hslider.dataTipFormatFunction = dataTipFormatter;
			hslider.maximum = 100;
			hslider.values = [0, 100];
			hslider.addEventListener(SliderEvent.THUMB_RELEASE, thumbRelease);
			lowField = new DateField();
			lowField.selectedDate = dateMetadata.earliestDate;
			lowField.addEventListener(CalendarLayoutChangeEvent.CHANGE, lowDateChange);
			highField = new DateField();
			highField.selectedDate = dateMetadata.latestDate;
			highField.addEventListener(CalendarLayoutChangeEvent.CHANGE, highDateChange);
            //if (!_filterEditable) {
            var checkbox:CheckBox = new CheckBox();
            checkbox.selected = _filterDefinition == null ? true : _filterDefinition.enabled;;
            checkbox.toolTip = "Click to disable this filter.";
            checkbox.addEventListener(Event.CHANGE, onChange);
            addChild(checkbox);
            //}
            if (_showLabel) {
                var label:Label = new Label();
                label.text = analysisItem.display + ":";
                addChild(label);
            } else {
                toolTip = analysisItem.display;
            }
            var leftSideStack:ViewStack = new ViewStack();
            BindingUtils.bindProperty(leftSideStack, "selectedIndex", this, "leftIndex");
            leftSideStack.resizeToContent = true;
            var leftFieldBox:Box = new Box();
            leftFieldBox.addChild(lowField);
            leftSideStack.addChild(leftFieldBox);
            var leftLabel:Label = new Label();
            BindingUtils.bindProperty(leftLabel, "text", this, "leftLabel");
            var leftBox:Box = new Box();
            leftBox.addChild(leftLabel);
            leftSideStack.addChild(leftBox);
            var rightSideStack:ViewStack = new ViewStack();
            BindingUtils.bindProperty(rightSideStack, "selectedIndex", this, "rightIndex");
            rightSideStack.resizeToContent = true;
            var rightFieldBox:Box = new Box();
            rightFieldBox.addChild(highField);
            rightSideStack.addChild(rightFieldBox);
            var rightLabel:Label = new Label();
            BindingUtils.bindProperty(rightLabel, "text", this, "rightLabel");
            var rightBox:Box = new Box();
            rightBox.addChild(rightLabel);
            rightSideStack.addChild(rightBox);
			addChild(leftSideStack);
			addChild(hslider);
			addChild(rightSideStack);
			if (_filterDefinition == null) {
				_filterDefinition = new FilterDateRangeDefinition();
				_filterDefinition.startDate = dateMetadata.earliestDate;
				_filterDefinition.endDate = dateMetadata.latestDate;
				_filterDefinition.field = analysisItem;
                //_filterDefinition.applyBeforeAggregation = false;
                _filterDefinition.sliding = true;
			} else {
                if (_filterDefinition.sliding && _filterDefinition.startDate != null && _filterDefinition.endDate != null) {
                    var nowDelta:int = dateMetadata.latestDate.getTime() - _filterDefinition.endDate.getTime();
                    _filterDefinition.startDate = new Date(_filterDefinition.startDate.getTime() + nowDelta);
                    _filterDefinition.endDate = new Date(_filterDefinition.endDate.getTime() + nowDelta);
                }
				if (_filterDefinition.startDate == null) {
					_filterDefinition.startDate = dateMetadata.earliestDate;
				}
				lowField.selectedDate = _filterDefinition.startDate;
				if (_filterDefinition.endDate == null) {
					_filterDefinition.endDate = dateMetadata.latestDate;
				}
                if (_filterDefinition.startDateDimension != null) {
                    leftIndex = 1;
                    this.leftLabel = _filterDefinition.startDateDimension.display;
                } else {
                    leftIndex = 0;
                }
                if (_filterDefinition.endDateDimension != null) {
                    rightIndex = 1;
                    this.rightLabel = _filterDefinition.endDateDimension.display;
                } else {
                    rightIndex = 0;
                }
				highField.selectedDate = _filterDefinition.endDate;
				var newLowVal:int = ((lowField.selectedDate.valueOf() - lowDate.valueOf()) / delta) * 100;
				var newHighVal:int = ((highField.selectedDate.valueOf() - lowDate.valueOf()) / delta) * 100;
				hslider.values = [ newLowVal, newHighVal ] ;
			}

            if (_filterEditable) {
                var editButton:Button = new Button();
                editButton.addEventListener(MouseEvent.CLICK, edit);
                editButton.setStyle("icon", editIcon);
                editButton.toolTip = "Edit";
                addChild(editButton);


                var deleteButton:Button = new Button();
                deleteButton.addEventListener(MouseEvent.CLICK, deleteSelf);
                deleteButton.setStyle("icon", deleteIcon);
                if (_filterDefinition.intrinsic) {
                    deleteButton.enabled = false;
                    deleteButton.toolTip = "This filter is an intrinsic part of the data source and cannot be deleted.";
                } else {
                    deleteButton.toolTip = "Delete";
                }
                addChild(deleteButton);
            }
			

            if (_loadingFromReport) {
                _loadingFromReport = false;

            } else {
			    dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_ADDED, filterDefinition, null, this));
            }
		}
		
		public function set analysisItems(analysisItems:ArrayCollection):void {
			_analysisItems = analysisItems;
		}		
		
		private function edit(event:MouseEvent):void {
			var window:GeneralFilterEditSettings = new GeneralFilterEditSettings();
			window.detailClass = DateRangeDetailEditor;
			window.addEventListener(FilterEditEvent.FILTER_EDIT, onFilterEdit, false, 0, true);
			window.analysisItems = _analysisItems;
			window.filterDefinition = _filterDefinition;
			PopUpManager.addPopUp(window, this, true);
			window.x = 50;
			window.y = 50;
		}
		
		private function onFilterEdit(event:FilterEditEvent):void {
            if (event.filterDefinition is FilterDateRangeDefinition) {
                var filter:FilterDateRangeDefinition = event.filterDefinition as FilterDateRangeDefinition;
                if (filter.startDateDimension != null) {
                    leftIndex = 1;
                    this.leftLabel = filter.startDateDimension.display;
                } else {
                    leftIndex = 0;
                }
                if (filter.endDateDimension != null) {
                    rightIndex = 1;
                    this.rightLabel = filter.endDateDimension.display;
                } else {
                    rightIndex = 0;
                }
            }
			dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, event.filterDefinition, event.previousFilterDefinition, this));
		}
		
		private function deleteSelf(event:MouseEvent):void {
			dispatchEvent(new FilterDeletionEvent(this));
		}

        private var _filterEditable:Boolean = true;

        public function set filterEditable(editable:Boolean):void {
            _filterEditable = editable;
        }
		
		private function thumbRelease(event:SliderEvent):void {
			var lowValue:int = hslider.values[0];
			var highValue:int = hslider.values[1];
			
			var newLowDate:Date = new Date();
			var newHighDate:Date = new Date();		
			
			newLowDate.setTime(lowDate.valueOf() + delta * (lowValue / 100));
			newHighDate.setTime(lowDate.valueOf() + delta * (highValue / 100));
			
			_filterDefinition.startDate = newLowDate;
			_filterDefinition.endDate = newHighDate;
			lowField.selectedDate = newLowDate;
			highField.selectedDate = newHighDate;			
			dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, filterDefinition, null, this));
		}
		
		private function lowDateChange(event:CalendarLayoutChangeEvent):void {
			var newLowVal:int = ((event.newDate.valueOf() - lowDate.valueOf()) / delta) * 100;			
			hslider.values = [ newLowVal, hslider.values[1] ] ;
			_filterDefinition.startDate = event.newDate;
			dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, filterDefinition, null, this));
		}
		
		private function highDateChange(event:CalendarLayoutChangeEvent):void {
			var newHighVal:int = ((event.newDate.valueOf() - lowDate.valueOf()) / delta) * 100;
			hslider.values = [ hslider.values[0], newHighVal ] ;
			_filterDefinition.endDate = event.newDate;
            _filterDefinition.sliding = false;
			dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, filterDefinition, null, this));
		}
		
		public function set filterDefinition(filterDefinition:FilterDefinition):void {
			this._filterDefinition = filterDefinition as FilterDateRangeDefinition;			
		}
		
		public function get filterDefinition():FilterDefinition {
			return _filterDefinition;
		}

        private var _showLabel:Boolean;

        public function set showLabel(show:Boolean):void {
            _showLabel = show;
        }
	}
}