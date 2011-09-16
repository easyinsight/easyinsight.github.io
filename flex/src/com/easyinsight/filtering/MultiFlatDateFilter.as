package com.easyinsight.filtering
{

	import com.easyinsight.analysis.AnalysisItem;


import flash.events.Event;
import flash.events.MouseEvent;
import flash.geom.Point;

import mx.collections.ArrayCollection;
import mx.containers.HBox;

import mx.controls.Button;
import mx.controls.CheckBox;
import mx.controls.Label;

import mx.controls.LinkButton;
import mx.managers.PopUpManager;

	public class MultiFlatDateFilter extends HBox implements IFilter
	{
		private var analysisItem:AnalysisItem;
		private var _filterDefinition:MultiFlatDateFilterDefinition;
		private var _analysisItems:ArrayCollection;

        private var _loadingFromReport:Boolean = false;


    public function set loadingFromReport(value:Boolean):void {
        _loadingFromReport = value;
    }

        private var _reportID:int;

    private var _dashboardID:int;

        private var _feedID:int;

    public function set reportID(value:int):void {
        _reportID = value;
    }

    public function set dashboardID(value:int):void {
        _dashboardID = value;
    }
		
		[Bindable]
        [Embed(source="../../../../assets/navigate_cross.png")]
        public var deleteIcon:Class;
        
        [Bindable]
        [Embed(source="../../../../assets/pencil.png")]
        public var editIcon:Class;
		
		public function MultiFlatDateFilter(feedID:int, analysisItem:AnalysisItem, reportID:int, dashboardID:int) {
			super();
			this.analysisItem = analysisItem;
            _feedID = feedID;
            _reportID = reportID;
            _dashboardID = dashboardID;
            setStyle("verticalAlign", "middle");
		}

        private function onChange(event:Event):void {
            var checkbox:CheckBox = event.currentTarget as CheckBox;
            _filterDefinition.enabled = checkbox.selected;
            dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, _filterDefinition, null, this));
        }

        private function onClick(event:Event):void {
            var window:MultiFWindow = new MultiFWindow();
            window.dateFilter = _filterDefinition;
            window.addEventListener("updated", onWindowDone, false, 0, true);
            var point:Point = new Point(this.x, this.y);
            var global:Point = localToGlobal(point);
            window.x = global.x;
            window.y = global.y;
            PopUpManager.addPopUp(window, this, true);
        }

        private function onWindowDone(event:Event):void {
            populateLabel();
            dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, _filterDefinition, null, this));
        }

        private function populateLabel():void {
            if (_filterDefinition.levels.length == 0) {
                labelButton.label = "(Click to Configure)";
            } else {
                var firstValue:int = 11;
                var lastValue:int = 0;
                for each (var wrapper:DateLevelWrapper in _filterDefinition.levels) {
                    firstValue = Math.min(wrapper.dateLevel, firstValue);
                    lastValue = Math.max(wrapper.dateLevel, lastValue);
                    var firstLabel:String = toMonthLabel(firstValue);
                    var secondLabel:String = toMonthLabel(lastValue);
                    if (firstLabel == secondLabel) {
                        labelButton.label = firstLabel;
                    } else {
                        labelButton.label = firstLabel + " to " + secondLabel;
                    }
                }

            }
        }

        private var labelButton:LinkButton;

        override protected function createChildren():void {
            super.createChildren();
            if (_filterDefinition == null || !_filterDefinition.toggleEnabled) {
                var checkbox:CheckBox = new CheckBox();
                checkbox.selected = _filterDefinition == null ? true : _filterDefinition.enabled;
                checkbox.toolTip = "Click to disable this filter.";
                checkbox.addEventListener(Event.CHANGE, onChange);
                addChild(checkbox);
            }

			if (_filterDefinition == null) {
				_filterDefinition = new MultiFlatDateFilterDefinition();
				_filterDefinition.field = analysisItem;
			} else {
			}

            var label:Label = new Label();
            if (_filterDefinition.filterName != null && _filterDefinition.filterName != "") {
                label.text = _filterDefinition.filterName + ":";
            } else {
                label.text = analysisItem.display + ":";
            }
            addChild(label);

            var firstValue:int = 11;
            var lastValue:int = 0;

            for each (var wrapper:DateLevelWrapper in _filterDefinition.levels) {
                firstValue = Math.min(wrapper.dateLevel, firstValue);
                lastValue = Math.max(wrapper.dateLevel, lastValue);
            }

            this.first = firstValue;
            this.last = lastValue;

            labelButton = new LinkButton();
            labelButton.setStyle("fontSize", 12);
            populateLabel();

            labelButton.setStyle("textDecoration", "underline");
            labelButton.addEventListener(MouseEvent.CLICK, onClick);
            addChild(labelButton);

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
                newFilter = false;
            } else {
			    if (newFilter) {
                    dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_ADDED, filterDefinition, null, this));
                    newFilter = false;
                } else {
                    dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, filterDefinition, filterDefinition, this));
                }
            }
        }

        private var first:int = -1;
        private var last:int = -1;

        private function toMonthLabel(value:int):String {
            var label:String;
            switch (value) {
                case 0:
                    label = "Jan";
                    break;
                case 1:
                    label = "Feb";
                    break;
                case 2:
                    label = "Mar";
                    break;
                case 3:
                    label = "Apr";
                    break;
                case 4:
                    label = "May";
                    break;
                case 5:
                    label = "Jun";
                    break;
                case 6:
                    label = "Jul";
                    break;
                case 7:
                    label = "Aug";
                    break;
                case 8:
                    label = "Sep";
                    break;
                case 9:
                    label = "Oct";
                    break;
                case 10:
                    label = "Nov";
                    break;
                case 11:
                    label = "Dec";
                    break;
            }
            return label;
        }

        private var newFilter:Boolean = true;
		
		public function set analysisItems(analysisItems:ArrayCollection):void {
			_analysisItems = analysisItems;
		}		
		
		private function edit(event:MouseEvent):void {
			var window:GeneralFilterEditSettings = new GeneralFilterEditSettings();
			window.addEventListener(FilterEditEvent.FILTER_EDIT, onFilterEdit, false, 0, true);
			window.analysisItems = _analysisItems;
			window.filterDefinition = _filterDefinition;
			PopUpManager.addPopUp(window, this, true);
			window.x = 50;
			window.y = 50;
		}
		
		private function onFilterEdit(event:FilterEditEvent):void {
            if (event.filterDefinition is FilterDateRangeDefinition) {

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
		
		public function set filterDefinition(filterDefinition:FilterDefinition):void {
			this._filterDefinition = filterDefinition as MultiFlatDateFilterDefinition;
		}
		
		public function get filterDefinition():FilterDefinition {
			return _filterDefinition;
		}

        private var _showLabel:Boolean;

        public function set showLabel(show:Boolean):void {
            _showLabel = show;
        }

        public function updateState():Boolean {
            var firstValue:int = 11;
            var lastValue:int;
            for each (var wrapper:DateLevelWrapper in _filterDefinition.levels) {
                firstValue = Math.min(wrapper.dateLevel, firstValue);
                lastValue = Math.max(wrapper.dateLevel, lastValue);
            }
            populateLabel();
            return (this.first != firstValue || this.last != lastValue);
        }
	}
}