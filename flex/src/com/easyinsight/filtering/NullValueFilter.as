package com.easyinsight.filtering {
import com.easyinsight.analysis.AnalysisItem;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.controls.Button;
import mx.controls.CheckBox;
import mx.controls.Label;
import mx.managers.PopUpManager;

public class NullValueFilter extends HBox implements IFilter {

    public static const LAST_VALUE:int = 1;
    public static const FIRST_VALUE:int = 2;
    public static const NULL_VALUE:int = 3;

    public function NullValueFilter(feedID:int, analysisItem:AnalysisItem, valueType:int) {
        super();
        _analysisItem = analysisItem;
        _feedID = feedID;
        _valueType = valueType;
    }

    private var _filterDefinition:FilterDefinition;
    private var _valueType:int;
		private var _analysisItem:AnalysisItem;
		private var _feedID:int;
		private var deleteButton:Button;
		private var editButton:Button;
		private var labelText:Label;
		private var _analysisItems:ArrayCollection;

		[Bindable]
        [Embed(source="../../../../assets/navigate_cross.png")]
        public var deleteIcon:Class;

        [Bindable]
        [Embed(source="../../../../assets/pencil.png")]
        public var editIcon:Class;

		public function set analysisItems(analysisItems:ArrayCollection):void {
			_analysisItems = analysisItems;
		}



    private var _loadingFromReport:Boolean = false;


    public function set loadingFromReport(value:Boolean):void {
        _loadingFromReport = value;
    }

		private function edit(event:MouseEvent):void {
			var window:GeneralFilterEditSettings = new GeneralFilterEditSettings();
            window.feedID = _feedID;
			window.addEventListener(FilterEditEvent.FILTER_EDIT, onFilterEdit, false, 0, true);
			window.analysisItems = _analysisItems;
			window.filterDefinition = _filterDefinition;
			PopUpManager.addPopUp(window, this, true);
			window.x = 50;
			window.y = 50;
		}

		private function onFilterEdit(event:FilterEditEvent):void {
			dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, event.filterDefinition, event.previousFilterDefinition, this, event.bubbles, event.rebuild));
		}

    private function onChange(event:Event):void {
            var checkbox:CheckBox = event.currentTarget as CheckBox;
            _filterDefinition.enabled = checkbox.selected;
            dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, _filterDefinition, null, this));
        }

		override protected function createChildren():void {
			super.createChildren();
            if (_filterDefinition == null) {
                if (_valueType == LAST_VALUE) _filterDefinition = new LastValueFilterDefinition();
                else if (_valueType == FIRST_VALUE) _filterDefinition = new FirstValueFilterDefinition();
                else if (_valueType == NULL_VALUE) _filterDefinition = new NullValueFilterDefinition();
                _filterDefinition.field = _analysisItem;
            }
            //if (!_filterEditable) {
                var checkbox:CheckBox = new CheckBox();
                checkbox.selected = _filterDefinition == null ? true : _filterDefinition.enabled;
                checkbox.toolTip = "Click to disable this filter.";
                checkbox.addEventListener(Event.CHANGE, onChange);
                addChild(checkbox);
            //}

            labelText = new Label();
            labelText.text = FilterDefinition.getLabel(_filterDefinition, _analysisItem);
            addChild(labelText);

            if (_filterEditable) {
                if (editButton == null) {
                    editButton = new Button();
                    editButton.addEventListener(MouseEvent.CLICK, edit);
                    editButton.setStyle("icon", editIcon);
                    editButton.toolTip = "Edit";
                }
                addChild(editButton);
                if (deleteButton == null) {
                    deleteButton = new Button();
                    deleteButton.addEventListener(MouseEvent.CLICK, deleteSelf);
                    deleteButton.setStyle("icon", deleteIcon);
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



		private function deleteSelf(event:MouseEvent):void {
			dispatchEvent(new FilterDeletionEvent(this));
		}

		public function get filterDefinition():FilterDefinition {
			return _filterDefinition;
		}

        private var _filterEditable:Boolean = true;

        public function set filterEditable(editable:Boolean):void {
            _filterEditable = editable;
        }

		public function set filterDefinition(filterDefinition:FilterDefinition):void {
			_filterDefinition = filterDefinition as LastValueFilterDefinition;
		}
}
}