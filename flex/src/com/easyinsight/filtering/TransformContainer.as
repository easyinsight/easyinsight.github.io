package com.easyinsight.filtering
{
import com.easyinsight.analysis.AnalysisChangedEvent;
import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.IRetrievalState;
import com.easyinsight.analysis.NamedKey;
import com.easyinsight.commands.CommandEvent;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.AnalysisItemWrapper;
import com.easyinsight.dashboard.Dashboard;
import com.easyinsight.dashboard.DashboardFilterOverride;
import com.easyinsight.util.PopUpUtil;

import flash.display.DisplayObject;
import flash.events.ContextMenuEvent;
import flash.ui.ContextMenu;
import flash.ui.ContextMenuItem;

import flash.utils.Dictionary;

import flexlib.containers.FlowBox;


import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.containers.VBox;

import mx.controls.AdvancedDataGrid;
import mx.controls.Alert;
import mx.controls.DataGrid;
import mx.controls.List;
import mx.core.UIComponent;
import mx.events.DragEvent;
import mx.events.FlexEvent;
import mx.managers.DragManager;
import mx.managers.PopUpManager;
import mx.states.RemoveChild;
import mx.states.State;

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

    public static const REPORT_EDITOR:int = 1;
    public static const DASHBOARD_EDITOR:int = 2;
    public static const DASHBOARD_STACK_EDITOR:int = 3;
    public static const FILTER_SET:int = 4;

    private var _context:int;

    public function set context(value:int):void {
        _context = value;
    }

    private var _retrievalState:IRetrievalState;

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


    public function set retrievalState(value:IRetrievalState):void {
        _retrievalState = value;
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

    protected static function adapterFlowBoxUpdateCompleteHandler(event:FlexEvent):void
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

    private var _dashboard:Dashboard;

    public function set dashboard(value:Dashboard):void {
        _dashboard = value;
    }

    private var _filterStorageKey:String;


    public function set filterStorageKey(value:String):void {
        _filterStorageKey = value;
    }

    private function createFilter(filterDefinition:FilterDefinition):IFilter {
        var filter:IFilter;
        var filterMetadata:FilterMetadata = new FilterMetadata();
        filterMetadata.key = _filterStorageKey;
        filterMetadata.context = _context;
        filterMetadata.filterSet = _filterSet;
        if (filterDefinition.getType() == FilterDefinition.VALUE) {
            var filterValueDefinition:FilterValueDefinition = filterDefinition as FilterValueDefinition;
            if (filterValueDefinition.singleValue) {
                if (filterValueDefinition.autoComplete) {
                    filter = new AutoCompleteFilter(_feedID, filterDefinition.field, _reportID, _dashboardID, _retrievalState, filterMetadata);
                } else {
                    filter = new ComboBoxFilter(_feedID, filterDefinition.field, _reportID, _dashboardID, _report,
                            _loadingFromReport ? ((_report != null && _report.filterDefinitions != null) ? _report.filterDefinitions : filterDefinitions) : filterDefinitions,
                            _dashboard, _retrievalState, filterMetadata);
                }
            } else {
                if (filterValueDefinition.newType) {
                    filter = new NewMultiValueFilter(_feedID, filterDefinition.field, _reportID,  _dashboardID,  _report,
                                            _loadingFromReport ? ((_report != null && _report.filterDefinitions != null) ? _report.filterDefinitions : filterDefinitions) : filterDefinitions,
                                            _dashboard, _retrievalState, filterMetadata);
                } else {
                    filter = new MultiValueFilter(_feedID, filterDefinition.field, _reportID, _dashboard, _retrievalState, filterMetadata);
                }
            }
        } else if (filterDefinition.getType() == FilterDefinition.DATE) {
            filter = new SliderDateFilter(_feedID, filterDefinition.field, _reportID, _dashboardID, _retrievalState, filterMetadata, _report);
        } else if (filterDefinition.getType() == FilterDefinition.RANGE) {
            filter = new SliderMeasureFilter(_feedID, filterDefinition.field, _retrievalState, filterMetadata);
        } else if (filterDefinition.getType() == FilterDefinition.ROLLING_DATE) {
            filter = new RollingRangeFilter(_feedID, filterDefinition.field, _retrievalState, filterMetadata);
        } else if (filterDefinition.getType() == FilterDefinition.LAST_VALUE) {
            filter = new GenericFilter(_feedID, filterDefinition.field, GenericFilter.LAST_VALUE, _retrievalState, filterMetadata);
        } else if (filterDefinition.getType() == FilterDefinition.FIRST_VALUE) {
            filter = new GenericFilter(_feedID, filterDefinition.field, GenericFilter.FIRST_VALUE, _retrievalState, filterMetadata);
        } else if (filterDefinition.getType() == FilterDefinition.NULL) {
            filter = new GenericFilter(_feedID, filterDefinition.field, GenericFilter.NULL_VALUE, _retrievalState, filterMetadata);
        } else if (filterDefinition.getType() == FilterDefinition.PATTERN) {
            filter = new PatternFilter(_feedID, filterDefinition.field, _retrievalState, filterMetadata);
        } else if (filterDefinition.getType() == FilterDefinition.OR) {
            filter = new OrFilterCanvas(_feedID, _analysisItems);
            filter.addEventListener(TransformsUpdatedEvent.UPDATED_TRANSFORMS, passThrough);
        } else if (filterDefinition.getType() == FilterDefinition.NAMED_REF) {
            filter = new GenericFilter(_feedID, filterDefinition.field, GenericFilter.NAMED_REF, _retrievalState, filterMetadata);
        } else if (filterDefinition.getType() == FilterDefinition.FLAT_DATE) {
            if(FlatDateFilterDefinition(filterDefinition).dateLevel == AnalysisItemTypes.MONTH_LEVEL)
                filter = new FlatMonthDateFilter(_feedID,  filterDefinition.field,  _reportID,  _dashboardID, _retrievalState, filterMetadata);
            else
                filter = new FlatDateFilter(_feedID,  filterDefinition.field, _reportID,  _dashboardID, _retrievalState, filterMetadata);
        } else if (filterDefinition.getType() == FilterDefinition.ANALYSIS_ITEM) {
            filter = new AnalysisItemFilter(_feedID, filterDefinition.field, _retrievalState, filterMetadata, _report, _dashboard);
        } else if (filterDefinition.getType() == FilterDefinition.MULTI_FLAT_DATE) {
            filter = new MultiFlatDateFilter(_feedID,  filterDefinition.field, _retrievalState, filterMetadata);
        } else if (filterDefinition.getType() == FilterDefinition.MONTH_CUTOFF) {
            filter = new MonthCutoffFilter(_feedID,  filterDefinition.field,  _reportID, _dashboardID);
        } else if (filterDefinition.getType() == FilterDefinition.MULTI_FIELD) {
            filter = new MultiFieldFilter(_feedID, _reportID, _dashboardID, _report, _dashboard, _retrievalState, filterMetadata);
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

    private var _filterSet:FilterSet;

    public function set filterSet(value:FilterSet):void {
        _filterSet = value;
    }

    public function addNewFilter(advancedAvailable:Boolean = true):void {
        var window:NewFilterWindow = new NewFilterWindow();
        if (_report != null) {
            window.reportType = _report.reportType;
        } else if (_context == FILTER_SET) {
            window.reportType = AnalysisDefinition.LIST;
        }
        var filterMetadata:FilterMetadata = new FilterMetadata();
        filterMetadata.filterSet = _filterSet;
        filterMetadata.key = _filterStorageKey;
        filterMetadata.context = _context;
        window.filterMetadata = filterMetadata;
        window.filterSource = _filterSource;
        window.availableFields = new ArrayCollection(this._analysisItems.toArray());
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
        } else if (event.filterType == NewFilterEvent.MULTI_FIELD_CHOICE_FILTER) {
            var multiFieldFilter:MultiFieldFilterDefinition = new MultiFieldFilterDefinition();
            multiFieldFilter.field = event.analysisItem;
            multiFieldFilter.availableHandles = new ArrayCollection();
            multiFieldFilter.toggleEnabled = true;
            addFilterDefinition(multiFieldFilter);
        }
    }

    public function createNewFilter(analysisItem:AnalysisItem, stageX:int, stageY:int):void {
        analysisItem = analysisItem.copy();
        var filterMetadata:FilterMetadata = new FilterMetadata();
        filterMetadata.key = _filterStorageKey;
        filterMetadata.context = _context;
        filterMetadata.filterSet = _filterSet;
        if (analysisItem.hasType(AnalysisItemTypes.DATE)) {
            var window:DateFilterWindow = new DateFilterWindow();
            window.filterMetadata = filterMetadata;
            window.report = _report;
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
            var sliderMeasureFilter:SliderMeasureFilter = new SliderMeasureFilter(_feedID, analysisItem, _retrievalState, filterMetadata);
            initializeFilter(sliderMeasureFilter, true);
        } else if (analysisItem.hasType(AnalysisItemTypes.DIMENSION)) {
            var dimWindow:GroupingFilterWindow = new GroupingFilterWindow();
            dimWindow.item = analysisItem;
            dimWindow.filterMetadata = filterMetadata;
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
        if (analysisItem != null) {
            analysisItem = analysisItem.copy();
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
            for each (var tObj1:Object in filterTile.getChildren()) {
                if (tObj1 is VBox) {
                    for each (var testFilter1:IFilter in VBox(tObj1).getChildren()) {
                        if (testFilter1.filterDefinition == event.filterDefinition) {
                            existingFilter = testFilter1;
                        }
                    }
                } else if (tObj1 is IFilter) {
                    if (IFilter(tObj1).filterDefinition == event.filterDefinition) {
                        existingFilter = IFilter(tObj1);
                    }
                }
            }
            for each (var filter:IFilter in filterTile.getChildren()) {
                if (filter.filterDefinition == event.previousFilterDefinition) {
                    existingFilter = filter;
                }
            }
            if (existingFilter != null) {
                UIComponent(existingFilter).parent.removeChild(existingFilter as DisplayObject);
            }
            var newFilter:IFilter = createFilter(event.filterDefinition);
            initializeFilter(newFilter, false);
        } else {
            var existingFilter1:IFilter = null;
            for each (var tObj:Object in filterTile.getChildren()) {
                if (tObj is VBox) {
                    for each (var testFilter:IFilter in VBox(tObj).getChildren()) {
                        if (testFilter.filterDefinition == event.filterDefinition) {
                            existingFilter1 = testFilter;
                        }
                    }
                } else if (tObj is IFilter) {
                    if (IFilter(tObj).filterDefinition == event.filterDefinition) {
                        existingFilter1 = IFilter(tObj);
                    }
                }
            }
            register(existingFilter1);
            var coll:Object = triggerMap[event.filter.filterDefinition.filterName];
            if (coll != null) {
                for each (var child:IFilter in coll) {
                    if (child is ComboBoxFilter) {
                        var label:String = null;
                        if (event.filter.filterDefinition != null && event.filter.filterDefinition is AnalysisItemFilterDefinition) {
                            label = AnalysisItemFilterDefinition(event.filter.filterDefinition).targetItem.display;
                        }
                        ComboBoxFilter(child).regenerate(label);
                    } else if (child is NewMultiValueFilter) {
                        NewMultiValueFilter(child).regenerate();
                    }

                }
            }
            dispatchEvent(new TransformsUpdatedEvent(filterDefinitions));
        }
    }

    private var hiddenButAvailableFilters:ArrayCollection = new ArrayCollection();

    private var showingHidden:Boolean = false;

    public function toggleHiddenButAvailableFilters():void {
        if (showingHidden) {
            for each (var filter:IFilter in hiddenButAvailableFilters) {
                filterTile.removeChild(filter as DisplayObject);
            }
        } else {
            for each (var filter:IFilter in hiddenButAvailableFilters) {
                filterTile.addChild(filter as DisplayObject);
            }
        }
        showingHidden = !showingHidden;
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

        // http://www.carrier-data.net/GPSWeb/Modules/Account/SmartelLogin.aspx
        if (!_reportView || (filter.filterDefinition.showOnReportView && roleVisible)) {
            if (_reportView && filter.filterDefinition != null && filter.filterDefinition.section != 0) {
                var section:VBox = sectionMap[String(filter.filterDefinition.section)];
                if (section == null) {
                    section = new VBox();
                    sectionMap[String(filter.filterDefinition.section)] = section;
                    filterTile.addChild(section);
                }
                section.addChild(filter as DisplayObject);
            } else {
                filterTile.addChild(filter as DisplayObject);
            }
        } else {
            if (!filter.filterDefinition.showOnReportView && filter.filterDefinition.customizable) {
                hiddenButAvailableFilters.addItem(filter);
            }
        }
        if (_loadingFromReport) {
            addFilter(filter);
        }

        if (filter.filterDefinition != null && filter.filterDefinition.filterID == 0 && filter.filterDefinition.fromFilterSet == 0) {
            dispatchEvent(new AnalysisChangedEvent());
        }
    }

    private var sectionMap:Object = new Object();

    private var triggerMap:Object = new Object();

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
            } else if (filter is NewMultiValueFilter) {
                NewMultiValueFilter(filter).showFilter(null);
            }
        }
        if (filter is ComboBoxFilter) {
            ComboBoxFilter(filter).otherFilters = filterDefinitions;
        }
    }

    private function initializeFilter(filter:IFilter, launchWindow:Boolean):void {
        dispatchEvent(new CommandEvent(new FilterAddCommand(this, filter, launchWindow)));
    }

    private function register(filter:IFilter):void {
        var filters:Array = filter.filterDefinition.retrieveParentFilters();
        for each (var filterName:String in filters) {
            var coll:Object = triggerMap[filterName];
            if (coll == null) {
                coll = new Object();
                triggerMap[filterName] = coll;
            }
            coll[filter.filterDefinition.filterName] = filter;
        }
        if (filter.filterDefinition.fieldChoiceFilterLabel != null && filter.filterDefinition.fieldChoiceFilterLabel != "") {
            var tFilterName:String = filter.filterDefinition.fieldChoiceFilterLabel;
            var coll1:Object = triggerMap[tFilterName];
            if (coll1 == null) {
                coll1 = new Object();
                triggerMap[tFilterName] = coll1;
            }
            coll1[tFilterName] = filter;
        }
    }

    protected function addFilter(filter:IFilter):void {
        //filterMap[filter.filterDefinition.field.qualifiedName()] = filter;
        register(filter);

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
        dispatchEvent(new TransformsUpdatedEvent(filterDefinitions));
    }

    public function commandFilterDelete(filter:IFilter):void {
        filterTile.removeChild(filter as DisplayObject);
        //delete filterMap[filter.filterDefinition.field.qualifiedName()];
        var index:int = filterDefinitions.getItemIndex(filter.filterDefinition);
        if (index != -1) {
            filterDefinitions.removeItemAt(index);
        }
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

    public function updateState():Boolean {
        var changed:Boolean = false;
        if (filterTile != null) {
            for each (var filter:IFilter in filterTile.getChildren()) {
                if (filter is ComboBoxFilter) {
                    changed = ComboBoxFilter(filter).updateState() || changed;
                } else if (filter is FlatDateFilter) {
                    changed = FlatDateFilter(filter).updateState() || changed;
                } else if (filter is MultiFlatDateFilter) {
                    changed = MultiFlatDateFilter(filter).updateState() || changed;
                } else if (filter is NewMultiValueFilter) {
                    changed = NewMultiValueFilter(filter).updateState() || changed;
                }
            }
        }
        return changed;
    }

    private var stateCtr:int = 0;

    private var filterOverrides:ArrayCollection;

    public function hideFilters(filters:ArrayCollection):void {

        var ops:Array = [];
        for each (var o:DashboardFilterOverride in filters) {
            if (o.hideFilter) {
                for each (var child:DisplayObject in filterTile.getChildren()) {
                    if (child is IFilter) {
                        var filter:IFilter = child as IFilter;
                        if (filter.filterDefinition.filterID == o.filterID) {
                            var remove:RemoveChild = new RemoveChild(DisplayObject(filter));
                            ops.push(remove);
                        }
                    }
                }
            }
        }
        var name:String = String(stateCtr++);
        if (ops.length > 0) {
            var overrideState:State = new State();
            overrideState.name = name;
            overrideState.overrides = ops;
            states = [overrideState];
            currentState = name;
        } else {
            currentState = "";
        }
        filterOverrides = filters;
        dispatchEvent(new TransformsUpdatedEvent(getVisibleFilterDefinitions(), false));
    }

    public function getVisibleFilterDefinitions():ArrayCollection {
        var filters:ArrayCollection = new ArrayCollection();
        for each (var filterDef:FilterDefinition in filterDefinitions) {
            var valid:Boolean = true;
            if (filterOverrides != null) {
                for each (var o:DashboardFilterOverride in filterOverrides) {
                    if (filterDef.filterID == o.filterID) {
                        valid = false;
                        break;
                    }
                }
            }
            if (valid) {
                filters.addItem(filterDef);
            }
        }

        return filters;
    }
}
}