package com.easyinsight.filtering
{
import com.easyinsight.WindowManagementInstance;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRetrievalState;
import com.easyinsight.framework.User;
import com.easyinsight.skin.ImageConstants;

import com.easyinsight.util.PopUpUtil;

import flash.events.Event;
import flash.events.FocusEvent;
import flash.events.KeyboardEvent;
import flash.events.MouseEvent;
import flash.ui.Keyboard;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
	import mx.containers.HBox;
	import mx.controls.Button;
import mx.controls.CheckBox;
import mx.controls.Label;
import mx.controls.LinkButton;
import mx.controls.TextInput;
import mx.core.UIComponent;
import mx.managers.PopUpManager;

	public class PatternFilter extends HBox implements IFilter
	{
		private var _filterDefinition:FilterPatternDefinition;
		private var _analysisItem:AnalysisItem;
		private var _feedID:int;		
		private var deleteButton:Button;
		private var labelText:UIComponent;
		private var _analysisItems:ArrayCollection;

        private var filterMetadata:FilterMetadata;

		public function PatternFilter(feedID:int, analysisItem:AnalysisItem, retrievalState:IRetrievalState, filterMetadata:FilterMetadata) {
			super();
			_analysisItem = analysisItem;
			_feedID = feedID;
            _retrievalState = retrievalState;
            this.filterMetadata = filterMetadata;
		}

        private var _loadingFromReport:Boolean = false;

        private var _retrievalState:IRetrievalState;

    public function set loadingFromReport(value:Boolean):void {
        _loadingFromReport = value;
    }
		
		public function set analysisItems(analysisItems:ArrayCollection):void {
			_analysisItems = analysisItems;
		}		
		
		public function edit(event:MouseEvent):void {
			var window:GeneralFilterEditSettings = new GeneralFilterEditSettings();
            window.filterMetadata = filterMetadata;
            window.feedID = _feedID;
			window.addEventListener(FilterEditEvent.FILTER_EDIT, onFilterEdit, false, 0, true);
            window.detailClass = PatternFilterWindow;
			window.analysisItems = _analysisItems;
			window.filterDefinition = _filterDefinition;
            WindowManagementInstance.getManager().addWindow(window);
			PopUpManager.addPopUp(window, this, true);
            PopUpUtil.centerPopUp(window);
		}
		
		private function onFilterEdit(event:FilterEditEvent):void {
            if (labelText != null && labelText is LinkButton) {
                LinkButton(labelText).label = FilterDefinition.getLabel(event.filterDefinition, event.filterDefinition.field);
            } else if (labelText != null && labelText is Label) {
                Label(labelText).text = FilterDefinition.getLabel(event.filterDefinition, event.filterDefinition.field);
            }
			dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, event.filterDefinition, event.previousFilterDefinition, this, event.bubbles, event.rebuild));
		}

        private function onChange(event:Event):void {
            var checkbox:CheckBox = event.currentTarget as CheckBox;
            _filterDefinition.enabled = checkbox.selected;
            try {
                if (_retrievalState != null) {
                    _retrievalState.updateFilter(_filterDefinition, filterMetadata);
                }
            } catch (e:Error) {
            }
            dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, _filterDefinition, null, this));
        }


		
		override protected function createChildren():void {
			super.createChildren();
            //if (!_filterEditable) {
            if (_filterDefinition == null || !_filterDefinition.toggleEnabled) {
                var checkbox:CheckBox = new CheckBox();
                checkbox.selected = _filterDefinition == null ? true : _filterDefinition.enabled;
                checkbox.toolTip = "Click to disable this filter.";
                checkbox.addEventListener(Event.CHANGE, onChange);
                addChild(checkbox);
            }
            //}
            if (_filterDefinition == null) {
                _filterDefinition = new FilterPatternDefinition();
                _filterDefinition.field = _analysisItem;
            }

            if (_filterEditable) {
                labelText = new LinkButton();
                labelText.styleName = "filterLabel";
                labelText.addEventListener(MouseEvent.CLICK, edit);
                LinkButton(labelText).label = FilterDefinition.getLabel(_filterDefinition, _analysisItem);
            } else {
                labelText = new Label();
                labelText.styleName = "filterLabel";
                Label(labelText).text = FilterDefinition.getLabel(_filterDefinition, _analysisItem);
            }

            addChild(labelText);

            valueLabel = new TextInput();
            if (User.getInstance() != null && User.getInstance().defaultFontFamily != null && User.getInstance().defaultFontFamily != "") {
                valueLabel.setStyle("fontFamily", User.getInstance().defaultFontFamily);
            }
            valueLabel.addEventListener(FocusEvent.FOCUS_OUT, onFocusOut);
            valueLabel.addEventListener(KeyboardEvent.KEY_UP, onKeyUp);
            BindingUtils.bindProperty(valueLabel, "text", filterDefinition, "pattern");
            valueLabel.text = _filterDefinition.pattern;
            addChild(valueLabel);
            if (_filterEditable) {
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

        private var valueLabel:TextInput;

        private function onKeyUp(event:KeyboardEvent):void {
            if (event.keyCode == Keyboard.ENTER) {
                var existing:String = _filterDefinition.pattern;
                if (existing != valueLabel.text) {
                    _filterDefinition.pattern = valueLabel.text;
                    dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, _filterDefinition, null, this));
                }
            }
        }

        private function onFocusOut(event:FocusEvent):void {
            var existing:String = _filterDefinition.pattern;
            if (existing != valueLabel.text) {
                _filterDefinition.pattern = valueLabel.text;
                dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, _filterDefinition, null, this));
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
			_filterDefinition = filterDefinition as FilterPatternDefinition; 	
		}
	}
}