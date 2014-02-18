package com.easyinsight.filtering {
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRetrievalState;
import com.easyinsight.skin.ImageConstants;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.controls.Alert;
import mx.controls.Button;
import mx.controls.CheckBox;
import mx.controls.Label;
import mx.managers.PopUpManager;

public class GenericFilter extends HBox implements IFilter {

    public static const LAST_VALUE:int = 1;
    public static const FIRST_VALUE:int = 2;
    public static const NULL_VALUE:int = 3;
    public static const NAMED_REF:int = 4;

    private var filterMetadata:FilterMetadata;

    public function GenericFilter(feedID:int, analysisItem:AnalysisItem, valueType:int, retrievalState:IRetrievalState, filterMetadata:FilterMetadata) {
        super();
        _analysisItem = analysisItem;
        this.filterMetadata = filterMetadata;
        _feedID = feedID;
        _valueType = valueType;
        _retrievalState = retrievalState;
    }

    private var _retrievalState:IRetrievalState;

    private var _filterDefinition:FilterDefinition;
    private var _valueType:int;
		private var _analysisItem:AnalysisItem;
		private var _feedID:int;
		private var deleteButton:Button;
		private var editButton:Button;
		private var labelText:Label;
		private var _analysisItems:ArrayCollection;

		public function set analysisItems(analysisItems:ArrayCollection):void {
			_analysisItems = analysisItems;
		}



    private var _loadingFromReport:Boolean = false;


    public function set loadingFromReport(value:Boolean):void {
        _loadingFromReport = value;
    }

		private function edit(event:MouseEvent):void {
			var window:GeneralFilterEditSettings = new GeneralFilterEditSettings();
            window.filterMetadata = filterMetadata;
            window.feedID = _feedID;
            if (_filterDefinition is LastValueFilterDefinition) {
                window.detailClass = LastValueFilterEditor;
            } else if (_filterDefinition is NamedFilterReference) {
                window.detailClass = NamedReferenceFilterEditor;
            }
			window.addEventListener(FilterEditEvent.FILTER_EDIT, onFilterEdit, false, 0, true);
			window.analysisItems = _analysisItems;
			window.filterDefinition = _filterDefinition;
			PopUpManager.addPopUp(window, this, true);
			window.x = 50;
			window.y = 50;
		}

		private function onFilterEdit(event:FilterEditEvent):void {
            labelText.text = NamedFilterReference(event.filterDefinition).referenceName;
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
                else if (_valueType == NAMED_REF) _filterDefinition = new NamedFilterReference();
                _filterDefinition.field = _analysisItem;
            }
            //if (!_filterEditable) {
                var checkbox:CheckBox = new CheckBox();
                checkbox.selected = _filterDefinition == null ? true : _filterDefinition.enabled;
                checkbox.toolTip = "Click to disable this filter.";
                checkbox.addEventListener(Event.CHANGE, onChange);
                addChild(checkbox);
            //}
            //if (_showLabel) {
                if (labelText == null) {
                    labelText = new Label();
                }
                if (_valueType == NAMED_REF) {
                    labelText.text = NamedFilterReference(_filterDefinition).referenceName;
                } else {
                    labelText.text = FilterDefinition.getLabel(_filterDefinition, _analysisItem);
                }
                labelText.styleName = "filterLabel";
                addChild(labelText);
            /*} else {
                if (_valueType == NAMED_REF) {
                    this.toolTip = NamedFilterReference(_filterDefinition).referenceName;
                } else {
                    this.toolTip = FilterDefinition.getLabel(_filterDefinition, _analysisItem);
                }
            }*/
            if (_filterEditable) {
                if (editButton == null) {
                    editButton = new Button();
                    editButton.addEventListener(MouseEvent.CLICK, edit);
                    editButton.setStyle("icon", ImageConstants.EDIT_ICON);
                    editButton.toolTip = "Edit";
                }
                addChild(editButton);
                if (deleteButton == null) {
                    deleteButton = new Button();
                    deleteButton.addEventListener(MouseEvent.CLICK, deleteSelf);
                    deleteButton.setStyle("icon", ImageConstants.DELETE_ICON);
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
			_filterDefinition = filterDefinition;
		}

        private var _showLabel:Boolean;

        public function set showLabel(show:Boolean):void {
            _showLabel = show;
        }
}
}