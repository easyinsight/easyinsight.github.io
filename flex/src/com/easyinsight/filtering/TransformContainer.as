package com.easyinsight.filtering
{
import com.easyinsight.analysis.AnalysisChangedEvent;
import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.NamedKey;
import com.easyinsight.commands.CommandEvent;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.AnalysisItemWrapper;
import com.easyinsight.util.PopUpUtil;

import flash.display.DisplayObject;
import flash.events.ContextMenuEvent;
import flash.ui.ContextMenu;
import flash.ui.ContextMenuItem;

import flash.utils.Dictionary;

import flexlib.containers.FlowBox;

import mx.collections.ArrayCollection;
import mx.containers.HBox;

import mx.controls.AdvancedDataGrid;
import mx.controls.Alert;
import mx.controls.DataGrid;
import mx.controls.List;
import mx.core.UIComponent;
import mx.events.DragEvent;
import mx.events.FlexEvent;
import mx.managers.DragManager;
import mx.managers.PopUpManager;

[Event(name="updatedTransforms", type="com.easyinsight.filtering.TransformsUpdatedEvent")]
[Event(name="analysisChanged", type="com.easyinsight.analysis.AnalysisChangedEvent")]

public class TransformContainer extends HBox
{
    private var filterMap:Dictionary = new Dictionary();
    private var filterDefinitions:ArrayCollection = new ArrayCollection();
    private var filterTile:FlowBox;
    private var _feedID:int;
    private var _reportID:int;
    private var _report:AnalysisDefinition;
    private var _dashboardID:int;
    private var noFilters:Boolean = true;
    private var _filterEditable:Boolean = true;
    [Bindable]
    private var _analysisItems:ArrayCollection;

    private var _filterDefinitions:ArrayCollection;

    private var showingFeedback:Boolean = false;

    public function TransformContainer() {
        super();
        this.addEventListener(DragEvent.DRAG_ENTER, dragEnterHandler);
        this.addEventListener(DragEvent.DRAG_DROP, dragDropHandler);
        this.addEventListener(DragEvent.DRAG_OVER, dragOverHandler);
        this.addEventListener(DragEvent.DRAG_EXIT, dragExitHandler);
        //setStyle("borderThickness", 1);
        //setStyle("borderStyle", "solid");
    }

    public function set report(value:AnalysisDefinition):void {
        _report = value;
    }

    public function set reportID(value:int):void {
        _reportID = value;
    }

    public function set dashboardID(value:int):void {
        _dashboardID = value;
    }

    public function reset():void {
        blah = false;
        filterMap = new Dictionary();
        filterDefinitions = new ArrayCollection();
        _filterDefinitions = new ArrayCollection();
        filterTile.removeAllChildren();
    }

    protected function adapterFlowBoxUpdateCompleteHandler(event:FlexEvent):void
    {
        // resize the FlowBox manually, as the internal calculation doesn't work

        var fb:FlowBox = event.target as FlowBox;

        if (fb != null)
        {
            if (fb.numChildren > 0)
            {
                // default the needed height to the top view metric

                var maxBottom:int = fb.viewMetrics.top;

                // Iterate over the children of the FlowBox to find the bottom-most bottom, so
                // we can determine how big / small we can make the FlowBox.
                // If it's a UIComponent, include it if the includeInLayout property is true.
                // If it's not a UIComponent, include it if the visible property is true.

                for (var idx:int = 0; idx < fb.numChildren; idx++)
                {
                    var displayObject:DisplayObject = fb.getChildAt(idx);

                    if ((displayObject is UIComponent && (displayObject as
                                                          UIComponent).includeInLayout) ||
                        (!(displayObject is UIComponent) && displayObject.visible))
                    {
                        var thisBottom:int = fb.getChildAt(idx).y + fb.getChildAt(idx).height;

                        if (thisBottom > maxBottom)
                            maxBottom = thisBottom;
                    }
                }

                fb.height = maxBottom + fb.viewMetrics.bottom +
                            fb.getStyle("paddingBottom") + 1;
            }
            else
            {
                fb.height = fb.viewMetrics.top + fb.viewMetrics.bottom;
            }
        }
    }

    public function set filterEditable(value:Boolean):void {
        _filterEditable = value;
    }

    private var blah:Boolean = false;

    public function set existingFilters(value:ArrayCollection):void {
        if (!blah && value != null) {
            loadingFromReport = true;
            blah = true;
            _filterDefinitions = value;
            if (filterTile != null) {
                for each (var filterDefinition:FilterDefinition in _filterDefinitions) {
                    addFilterDefinition(filterDefinition);
                }
            }
            loadingFromReport = false;
        }
    }

    public function clearFilter(item:AnalysisItem):void {
        for each (var filter:IFilter in filterMap) {
            if (filter.filterDefinition.field.qualifiedName() == item.qualifiedName()) {
                filterTile.removeChild(filter as DisplayObject);
                var index:int = filterDefinitions.getItemIndex(filter.filterDefinition);
                filterDefinitions.removeItemAt(index);
                if (filterDefinitions.length == 0) {
                    noFilters = true;
                }
            }
        }
        dispatchEvent(new TransformsUpdatedEvent(filterDefinitions));
    }

    public function set analysisItems(analysisItems:ArrayCollection):void {
        _analysisItems = analysisItems;
    }

    override protected function createChildren():void {
        super.createChildren();
        if (filterTile == null) {
            filterTile = new FlowBox();
            filterTile.setStyle("horizontalGap", 20);
            filterTile.addEventListener(FlexEvent.UPDATE_COMPLETE, adapterFlowBoxUpdateCompleteHandler);
            filterTile.percentWidth = 100;
            filterTile.percentHeight = 100;
        }
        addChild(filterTile);
        if (noFilters && _filterDefinitions == null) {

        } else {
            loadingFromReport = true;
            if (_filterDefinitions != null) {
                for each (var filter:FilterDefinition in _filterDefinitions) {
                    addFilterDefinition(filter);
                }
            }
            loadingFromReport = false;
        }
        if (!_reportView) {
            var contextMenu:ContextMenu = new ContextMenu();
            var manageFiltersItem:ContextMenuItem = new ContextMenuItem("Manage Filters");
            manageFiltersItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, manageFilters);
            contextMenu.customItems = [ manageFiltersItem ];
            this.contextMenu = contextMenu;
        }
    }

    private function manageFilters(event:ContextMenuEvent):void {
        reorderFilters();
    }

    protected var _loadingFromReport:Boolean = false;

    public function set loadingFromReport(value:Boolean):void {
        _loadingFromReport = value;
    }

    private var _reportView:Boolean = false;

    public function set reportView(value:Boolean):void {
        _reportView = value;
    }

    public function addFilterDefinition(filterDefinition:FilterDefinition):IFilter {
        var filter:IFilter = createFilter(filterDefinition);
        commandFilterAdd(filter);
        return filter;
    }

    private function createFilter(filterDefinition:FilterDefinition):IFilter {
        var filter:IFilter;
        if (filterDefinition.getType() == FilterDefinition.VALUE) {
            var filterValueDefinition:FilterValueDefinition = filterDefinition as FilterValueDefinition;
            if (filterValueDefinition.singleValue) {
                if (filterValueDefinition.autoComplete) {
                    filter = new AutoCompleteFilter(_feedID, filterDefinition.field, _reportID, _dashboardID);
                } else {
                    filter = new ComboBoxFilter(_feedID, filterDefinition.field, _reportID, _dashboardID, _report);
                }
            } else {
                filter = new MultiValueFilter(_feedID, filterDefinition.field);
            }
        } else if (filterDefinition.getType() == FilterDefinition.DATE) {
            filter = new SliderDateFilter(_feedID, filterDefinition.field, _reportID, _dashboardID);
        } else if (filterDefinition.getType() == FilterDefinition.RANGE) {
            filter = new SliderMeasureFilter(_feedID, filterDefinition.field);
        } else if (filterDefinition.getType() == FilterDefinition.ROLLING_DATE) {
            filter = new RollingRangeFilter(_feedID, filterDefinition.field);
        } else if (filterDefinition.getType() == FilterDefinition.LAST_VALUE) {
            filter = new GenericFilter(_feedID, filterDefinition.field, GenericFilter.LAST_VALUE);
        } else if (filterDefinition.getType() == FilterDefinition.FIRST_VALUE) {
            filter = new GenericFilter(_feedID, filterDefinition.field, GenericFilter.FIRST_VALUE);
        } else if (filterDefinition.getType() == FilterDefinition.NULL) {
            filter = new GenericFilter(_feedID, filterDefinition.field, GenericFilter.NULL_VALUE);
        } else if (filterDefinition.getType() == FilterDefinition.PATTERN) {
            filter = new PatternFilter(_feedID, filterDefinition.field);
        } else if (filterDefinition.getType() == FilterDefinition.OR) {
            filter = new OrFilterCanvas(_feedID);
            filter.addEventListener(TransformsUpdatedEvent.UPDATED_TRANSFORMS, passThrough);
        } else if (filterDefinition.getType() == FilterDefinition.NAMED_REF) {
            filter = new GenericFilter(_feedID, filterDefinition.field, GenericFilter.NAMED_REF);
        } else if (filterDefinition.getType() == FilterDefinition.FLAT_DATE) {
            filter = new FlatDateFilter(_feedID,  filterDefinition.field, _reportID,  _dashboardID);
        } else if (filterDefinition.getType() == FilterDefinition.ANALYSIS_ITEM) {
            filter = new AnalysisItemFilter(_feedID, filterDefinition.field);
        } else if (filterDefinition.getType() == FilterDefinition.MULTI_FLAT_DATE) {
            filter = new MultiFlatDateFilter(_feedID,  filterDefinition.field);
        } else if (filterDefinition.getType() == FilterDefinition.MONTH_CUTOFF) {
            filter = new MonthCutoffFilter(_feedID,  filterDefinition.field,  _reportID, _dashboardID);
        } else {
            Alert.show("unknown filter type = " + filterDefinition.getType());
        }
        filter.filterEditable = _filterEditable;
        filter.filterDefinition = filterDefinition;
        return filter;
    }

    private function passThrough(event:TransformsUpdatedEvent):void {
        dispatchEvent(new TransformsUpdatedEvent(this.filterDefinitions));
    }

    public function set feedID(feedID:int):void {
        this._feedID = feedID;
    }

    protected function dragOverHandler(event:DragEvent):void {
        if (!showingFeedback) {
            DragManager.showFeedback(DragManager.MOVE);
            showingFeedback = true;
        }
    }

    protected function dragEnterHandler(event:DragEvent):void {
        var analysisItem:AnalysisItem;
        if (event.dragInitiator is DataGrid) {
            var initialList:DataGrid = DataGrid(event.dragInitiator);
            analysisItem = AnalysisItemWrapper(initialList.selectedItem).analysisItem;
        } else if (event.dragInitiator is List) {
            var list:List = List(event.dragInitiator);
            analysisItem = list.selectedItem as AnalysisItem;
        } else if (event.dragInitiator is AdvancedDataGrid) {
            var analysisItemLabel:AdvancedDataGrid = event.dragInitiator as AdvancedDataGrid;
            var wrapper:AnalysisItemWrapper = analysisItemLabel.selectedItem as AnalysisItemWrapper;
            if (wrapper.isAnalysisItem()) {
                analysisItem = wrapper.analysisItem;
            }
        }
        if (analysisItem != null) {
            setStyle("borderColor", "green");
            DragManager.acceptDragDrop(event.target as TransformContainer);
        }
    }

    protected function dragExitHandler(event:DragEvent):void {
        setStyle("borderColor", 0xFFFFFF);
        showingFeedback = false;
    }

    private var _filterSource:int;

    public function set filterSource(value:int):void {
        _filterSource = value;
    }

    public function addNewFilter(advancedAvailable:Boolean = true):void {
        var window:NewFilterWindow = new NewFilterWindow();
        window.filterSource = _filterSource;
        window.availableFields = this._analysisItems;
        window.advancedAvailable = advancedAvailable;
        window.addEventListener(NewFilterEvent.NEW_FILTER, onFilterCreation, false, 0, true);
        PopUpManager.addPopUp(window, this, true);
        PopUpUtil.centerPopUp(window);
    }

    private function onFilterCreation(event:NewFilterEvent):void {
        if (event.filterType == NewFilterEvent.FIELD_FILTER) {
            createNewFilter(event.analysisItem, 0, 0);
        } else if (event.filterType == NewFilterEvent.OR_FILTER) {
            var orFilter:OrFilter = new OrFilter();
            addFilterDefinition(orFilter);
        } else if (event.filterType == NewFilterEvent.NAMED_REF_FILTER) {
            var namedRefFilter:NamedFilterReference = new NamedFilterReference();
            addFilterDefinition(namedRefFilter);
        } else if (event.filterType == NewFilterEvent.TREND_FILTER) {
            var tempField:AnalysisDateDimension = new AnalysisDateDimension();
            tempField.displayName = "Date";
            var key:NamedKey = new NamedKey();
            key.name = "Date";
            tempField.key = key;

            var filter:RollingDateRangeFilterDefinition = new RollingDateRangeFilterDefinition();
            filter.trendFilter = true;
            filter.filterName = "Trend Date";
            filter.interval = RollingDateRangeFilterDefinition.WEEK;
            filter.field = tempField;
            addFilterDefinition(filter);
        } else if (event.filterType == NewFilterEvent.FIELD_CHOICE_FILTER) {
            var analysisFilter:AnalysisItemFilterDefinition = new AnalysisItemFilterDefinition();
            analysisFilter.field = event.analysisItem;
            analysisFilter.targetItem = event.analysisItem;
            analysisFilter.availableItems = new ArrayCollection();
            analysisFilter.availableItems.addItem(event.analysisItem);
            analysisFilter.toggleEnabled = true;
            addFilterDefinition(analysisFilter);
        }
    }

    public function createNewFilter(analysisItem:AnalysisItem, stageX:int, stageY:int):void {
        if (analysisItem.hasType(AnalysisItemTypes.DATE)) {
            var window:DateFilterWindow = new DateFilterWindow();
            window.feedID = _feedID;
            window.item = analysisItem;
            window.addEventListener(FilterCreationEvent.FILTER_CREATION, onFilterSelection, false, 0, true);
            PopUpManager.addPopUp(window, this, true);
            //Alert.show(event.localX + " - " + event.localY);
            if (stageX == 0 && stageY == 0) {
                PopUpUtil.centerPopUp(window);
            } else {
                var x:int = stageX - 220;
                x = Math.max(x, 30);
                window.x = x;
                window.y = stageY - 35;
            }
        } else if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
            var sliderMeasureFilter:SliderMeasureFilter = new SliderMeasureFilter(_feedID, analysisItem);
            initializeFilter(sliderMeasureFilter, true);
        } else if (analysisItem.hasType(AnalysisItemTypes.DIMENSION)) {
            var dimWindow:GroupingFilterWindow = new GroupingFilterWindow();
            dimWindow.item = analysisItem;
            dimWindow.report = _report;
            dimWindow.feedID = _feedID;
            dimWindow.addEventListener(FilterCreationEvent.FILTER_CREATION, onFilterSelection, false, 0, true);
            PopUpManager.addPopUp(dimWindow, this, true);
            if (stageX == 0 && stageY == 0) {
                PopUpUtil.centerPopUp(dimWindow);
            } else {
                dimWindow.x = Math.max(stageX - 220, 30);
                dimWindow.y = stageY - 35;
            }
        }
    }

    protected function getBorderColor():uint {
        return 0xFFFFFF;
    }

    protected function dragDropHandler(event:DragEvent):void {
        setStyle("borderColor", getBorderColor());
        showingFeedback = false;
        var analysisItem:AnalysisItem;
        if (event.dragInitiator is DataGrid) {
            var initialList:DataGrid = DataGrid(event.dragInitiator);
            analysisItem = AnalysisItemWrapper(initialList.selectedItem).analysisItem;
        } else if (event.dragInitiator is List) {
            var list:List = List(event.dragInitiator);
            analysisItem = list.selectedItem as AnalysisItem;
        } else if (event.dragInitiator is AdvancedDataGrid) {
            var analysisItemLabel:AdvancedDataGrid = event.dragInitiator as AdvancedDataGrid;
            var wrapper:AnalysisItemWrapper = analysisItemLabel.selectedItem as AnalysisItemWrapper;
            if (wrapper.isAnalysisItem()) {
                analysisItem = wrapper.analysisItem;
            }
        }
        createNewFilter(analysisItem, event.stageX, event.stageY);
    }

    private function onFilterSelection(event:FilterCreationEvent):void {
        initializeFilter(event.filter, true);
    }
    
    public function reorderFilters():void {
        var window:FilterOrderWindow = new FilterOrderWindow();
        window.filterDefinitions = getFilterDefinitions();
        window.addEventListener(FilterReorderEvent.FILTER_REORDER, onReorder, false, 0, true);
        PopUpManager.addPopUp(window, this, true);
        PopUpUtil.centerPopUp(window);
    }

    private function onReorder(event:FilterReorderEvent):void {
        reset();
        _filterDefinitions = event.filters;
        loadingFromReport = true;
        if (_filterDefinitions != null) {
            for each (var filter:FilterDefinition in _filterDefinitions) {
                addFilterDefinition(filter);
            }
        }
        loadingFromReport = false;
    }

    private function filterAdded(event:FilterUpdatedEvent):void {
        addFilter(event.filter);
        dispatchEvent(new TransformsUpdatedEvent(filterDefinitions));
    }

    private function filterUpdated(event:FilterUpdatedEvent):void {
        if (!filterDefinitions.contains(event.filterDefinition) || event.rebuild) {
            var index:int = filterDefinitions.getItemIndex(event.previousFilterDefinition);
            filterDefinitions.removeItemAt(index);
            var existingFilter:IFilter = null;
            for each (var filter:IFilter in filterTile.getChildren()) {
                if (filter.filterDefinition == event.previousFilterDefinition) {
                    existingFilter = filter;
                }
            }
            if (existingFilter != null) {
                filterTile.removeChild(existingFilter as DisplayObject);
            }
            var newFilter:IFilter = createFilter(event.filterDefinition);
            initializeFilter(newFilter, false);
        } else {
            dispatchEvent(new TransformsUpdatedEvent(filterDefinitions));
        }
    }

    public function commandFilterAdd(filter:IFilter):void {
        if (noFilters) {
            noFilters = false;
        }
        filter.filterEditable = _filterEditable;
        filter.loadingFromReport = _loadingFromReport;
        filter.addEventListener(FilterUpdatedEvent.FILTER_ADDED, filterAdded);
        filter.addEventListener(FilterUpdatedEvent.FILTER_UPDATED, filterUpdated);
        filter.addEventListener(FilterDeletionEvent.DELETED_FILTER, filterDeleted);
        var roleVisible:Boolean = _role == 0 || _role <= filter.filterDefinition.minimumRole;
        if (!_reportView || (filter.filterDefinition.showOnReportView && roleVisible)) {
            filterTile.addChild(filter as DisplayObject);
        }
        if (_loadingFromReport) {
            addFilter(filter);
        }
        if (filter.filterDefinition != null && filter.filterDefinition.filterID == 0) {
            dispatchEvent(new AnalysisChangedEvent());
        }
    }

    private var _role:int;

    public function set role(value:int):void {
        _role = value;
    }

    public function commandFilterAdd2(filter:IFilter, launchWindow:Boolean):void {
        commandFilterAdd(filter);
        if (launchWindow) {
            if (filter is SliderMeasureFilter) {
                SliderMeasureFilter(filter).edit(null);
            } else if (filter is PatternFilter) {
                PatternFilter(filter).edit(null);
            } else if (filter is MultiValueFilter) {
                MultiValueFilter(filter).edit(null);
            }
        }
    }

    private function initializeFilter(filter:IFilter, launchWindow:Boolean):void {
        dispatchEvent(new CommandEvent(new FilterAddCommand(this, filter, launchWindow)));
    }

    protected function addFilter(filter:IFilter):void {
        //filterMap[filter.filterDefinition.field.qualifiedName()] = filter;
        filterDefinitions.addItem(filter.filterDefinition);
        filter.analysisItems = _analysisItems;
    }

    public function getFilterDefinitions():ArrayCollection {
        return filterDefinitions;
    }

    public function removeFilter(filterDefinition:FilterDefinition):void {
        var tFilter:IFilter;
        for each (var child:DisplayObject in filterTile.getChildren()) {
            if (child is IFilter) {
                var filter:IFilter = child as IFilter;
                if (filter.filterDefinition == filterDefinition) {
                    tFilter = filter;
                    filterTile.removeChild(child);
                }
            }
        }
        var index:int = filterDefinitions.getItemIndex(tFilter.filterDefinition);
        filterDefinitions.removeItemAt(index);
        if (filterDefinitions.length == 0) {
            noFilters = true;
        }
    }

    public function commandFilterDelete(filter:IFilter):void {
        filterTile.removeChild(filter as DisplayObject);
        //delete filterMap[filter.filterDefinition.field.qualifiedName()];
        var index:int = filterDefinitions.getItemIndex(filter.filterDefinition);
        filterDefinitions.removeItemAt(index);
        if (filterDefinitions.length == 0) {
            noFilters = true;
            //removeChild(filterTile);
            //addChild(dropHereBox);
        }
        dispatchEvent(new TransformsUpdatedEvent(filterDefinitions));
    }

    private function filterDeleted(event:FilterDeletionEvent):void {
        dispatchEvent(new CommandEvent(new FilterDeleteCommand(this, event.getFilter())));
    }

    public function processRawFilterData(filterRawData:FilterRawData, includeFilter:Boolean):void {
        for (var i:int = 0; i < filterRawData.getKeys().length; i++) {
            var key:AnalysisItem = filterRawData.getKeys().getItemAt(i) as AnalysisItem;
            var values:ArrayCollection = filterRawData.getValues(key);
            var uniqueValues:ArrayCollection = new ArrayCollection();
            for each (var val:Object in values) {
                if (uniqueValues.getItemIndex(val) == -1) {
                    uniqueValues.addItem(val);
                }
            }
            var filter:IFilter = null;
                // doesn't exist yet, create it
                var filterDefinition:FilterDefinition;
                if (key.hasType(AnalysisItemTypes.DATE)) {
                    var filterDateRangeDefinition:FilterDateRangeDefinition = new FilterDateRangeDefinition();
                    filterDateRangeDefinition.field = key;
                    filterDefinition = filterDateRangeDefinition;
                    filter = new SliderDateFilter(_feedID, key, _reportID, _dashboardID);
                } else if (key.hasType(AnalysisItemTypes.DIMENSION)) {
                    var filterValueDefinition:FilterValueDefinition = new FilterValueDefinition();
                    filterValueDefinition.field = key;
                    filterValueDefinition.filteredValues = uniqueValues;
                    filterValueDefinition.inclusive = includeFilter;
                    filterDefinition = filterValueDefinition;
                    if (values.length == 1 && includeFilter) {
                        filter = new ComboBoxFilter(_feedID, key, _reportID, _dashboardID, _report);
                    } else {
                        filter = new MultiValueFilter(_feedID, key);
                    }
                } else if (key.hasType(AnalysisItemTypes.MEASURE)) {
                    var filterMeasureRangeDefinition:FilterRangeDefinition = new FilterRangeDefinition();
                    filterMeasureRangeDefinition.field = key;
                    filterDefinition = filterMeasureRangeDefinition;
                    filter = new SliderMeasureFilter(_feedID, key);
                    var min:Number = Number.MAX_VALUE;
                    var max:Number = Number.MIN_VALUE;
                    for each (var valO:Object in uniqueValues) {
                        var num:Number = Number(valO);
                        if (num < min) {
                            min = num;
                        }
                        if (num > max) {
                            max = num;
                        }
                    }
                    if (includeFilter) {
                        filterMeasureRangeDefinition.currentEndValueDefined = true;
                        filterMeasureRangeDefinition.currentEndValue = max;
                        filterMeasureRangeDefinition.currentStartValueDefined = true;
                        filterMeasureRangeDefinition.currentStartValue = min;
                    } else {

                    }
                }
                filterMap[key.qualifiedName()] = filter;
                filter.filterDefinition = filterDefinition;
                initializeFilter(filter, true);

        }
    }

    public function updateState():Boolean {
        var changed:Boolean = false;
        for each (var filter:IFilter in filterTile.getChildren()) {
            if (filter is ComboBoxFilter) {
                changed = ComboBoxFilter(filter).updateState() || changed;
            } else if (filter is FlatDateFilter) {
                changed = FlatDateFilter(filter).updateState() || changed;
            } else if (filter is MultiFlatDateFilter) {
                changed = MultiFlatDateFilter(filter).updateState() || changed;
            }
        }
        return changed;
    }
}
}