/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/1/11
 * Time: 2:08 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.reportviews {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.dashboard.DashboardEditorMetadata;
import com.easyinsight.dashboard.DashboardElement;
import com.easyinsight.dashboard.DashboardReport;
import com.easyinsight.dashboard.DashboardStack;
import com.easyinsight.dashboard.DashboardStackItem;
import com.easyinsight.dashboard.IDashboardViewComponent;
import com.easyinsight.dashboard.SizeInfo;
import com.easyinsight.filter.FilterFactory;
import com.easyinsight.filtering.FilterDefinition;
import com.easyinsight.skin.ImageLoadEvent;
import com.easyinsight.skin.ImageLoader;
import com.easyinsight.util.EIComboBox;
import com.easyinsight.util.ExportWindow;
import com.easyinsight.util.FilterChangeEvent;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.core.FlexGlobals;
import mx.core.UIComponent;
import mx.events.FlexEvent;
import mx.graphics.SolidColor;
import mx.states.AddItems;
import mx.states.SetProperty;
import mx.states.State;

import org.flexlayouts.layouts.FlowLayout;

import spark.components.Button;

import spark.components.Group;
import spark.components.HGroup;
import spark.components.Image;

import spark.components.VGroup;
import spark.primitives.Rect;

public class DashboardStackMobileComponent extends VGroup implements IDashboardViewComponent {

    public var dashboardStack:DashboardStack;

    public var dashboardEditorMetadata:DashboardEditorMetadata;

    public function DashboardStackMobileComponent() {
        super();
        this.percentWidth = 100;
        this.percentHeight = 100;
    }

    private function styleHeaderArea(headerArea:Group):Group {
        if (dashboardStack.headerBackgroundAlpha > 0) {
            var fillRect:Rect = new Rect();
            fillRect.percentHeight = 100;
            fillRect.percentWidth = 100;
            fillRect.fill = new SolidColor(dashboardStack.headerBackgroundColor, dashboardStack.headerBackgroundAlpha);
            headerArea.addElement(fillRect);
        }
        headerArea.percentWidth = 100;
        var headerBackgroundImageArea:Group = new Group();
        headerBackgroundImageArea.percentWidth = 100;
        var headerBackgroundImage:Image = new Image();
        var headerbar:HGroup = new HGroup();
        if (dashboardStack.headerBackground != null) {
            var headerBarLoader:ImageLoader = new ImageLoader();
            headerBarLoader.addEventListener(ImageLoadEvent.IMAGE_LOADED, function(event:ImageLoadEvent):void {
                headerBackgroundImage.width = event.bitmap.width;
                headerBackgroundImage.height = event.bitmap.height;
                headerBackgroundImage.source = event.bitmap;
            });
            headerBarLoader.load(dashboardStack.headerBackground.id, "https://www.easy-insight.com/app/messagebroker/amfsecure");
            logoutButton = new Button();
            logoutButton.height = 30;
            logoutButton.label = "Log Out";
            logoutButton.addEventListener(MouseEvent.CLICK, logout);
            shareButton = new Button();
            shareButton.label = "Share";
            shareButton.height = 30;
            shareButton.addEventListener(MouseEvent.CLICK, share);
        }
        headerbar.percentWidth = 100;
        headerbar.percentHeight = 100;
        headerbar.paddingRight = 10;
        if (dashboardStack.headerBackground != null) {
            headerbar.horizontalAlign = "right";
        } else {
            headerbar.horizontalAlign = "center";
        }
        headerbar.verticalAlign = "bottom";
        headerbar.setStyle("paddingBottom", 5);
        headerBackgroundImageArea.addElement(headerBackgroundImage);
        headerBackgroundImageArea.addElement(headerbar);
        headerArea.addElement(headerBackgroundImageArea);
        if (logoutButton != null) {
            headerBackgroundImageArea.addElement(shareButton);
            headerBackgroundImageArea.addElement(logoutButton);
        }
        return headerbar;
    }

    private var logoutButton:Button;
    private var shareButton:Button;

    private function share(event:MouseEvent):void {
        var button:Button = event.currentTarget as Button;
        var reports:ArrayCollection = reportCount();
        if (reports.length == 1) {
            var report:AnalysisDefinition = reports.getItemAt(0) as AnalysisDefinition;
            var window:ExportWindow = new ExportWindow();
            window.report = report;
            window.open(button, true);
        }
    }

    private function logout(event:MouseEvent):void {
        dashboardEditorMetadata.dashboardView.dispatchEvent(new LogoutEvent());
    }

    private function createStackChildren(headerbar:Group):void {
        if (dashboardStack.selectionType == 'Buttons') {
            if (dashboardStack.gridItems.length > 1) {
                var buttonBar:HGroup = new HGroup();
                buttonBar.paddingBottom = 5;
                for (var i:int = 0; i < dashboardStack.gridItems.length; i++) {
                    var stackItem:DashboardStackItem = dashboardStack.gridItems.getItemAt(i) as DashboardStackItem;
                    var element:DashboardElement = stackItem.dashboardElement;
                    var label:String;
                    if (element is DashboardReport) {
                        label = DashboardReport(element).report.name;
                    } else {
                        if (element.label != null && element.label != "") {
                            label = element.label;
                        } else {
                            label = String(i);
                        }
                    }
                    var button:DashboardButton = new DashboardButton(i);
                    button.height = 30;
                    button.label = label;
                    button.addEventListener(MouseEvent.CLICK, onChange);
                    buttonBar.addElement(button);
                }
                headerbar.addElement(buttonBar);
            }
        } else {
            var childComboBox:EIComboBox = new EIComboBox();
            childComboBox.labelFunction = comboBoxLabelFunction;
            childComboBox.dataProvider = dashboardStack.gridItems;
            childComboBox.addEventListener(Event.CHANGE, onComboBoxChange);
            headerbar.addElement(childComboBox);
        }
    }

    private function comboBoxLabelFunction(object:Object):String {
        var report:DashboardElement = DashboardStackItem(object).dashboardElement;
        if (report is DashboardReport) {
            return DashboardReport(report).report.name;
        } else {
            if (report.label != null && report.label != "") {
                return report.label;
            } else {
                return "(Unlabeled)";
            }
        }
    }

    private function onComboBoxChange(event:Event):void {
        var index:int = dashboardStack.gridItems.getItemIndex(EIComboBox(event.currentTarget).selectedValue);
    }

    private var headerHGroup:HGroup;

    override protected function createChildren():void {
        super.createChildren();
        trace("creating stack with label " + dashboardStack.label);
        if (dashboardStack.consolidateHeaderElements) {
            headerHGroup = new HGroup();
            headerHGroup.paddingLeft = 10;
            headerHGroup.paddingRight = 10;
            headerHGroup.percentWidth = 100;
            childFiltersBox = new HGroup();
            childFiltersBox.percentWidth = 100;
            /*var myFiltersBox:HGroup = new HGroup();
            headerHGroup.addElement(myFiltersBox);*/
            headerHGroup.addElement(childFiltersBox);
            var buttonsBox:HGroup = new HGroup();
            headerHGroup.addElement(buttonsBox);
            addElement(headerHGroup);
        } else {
            if (dashboardStack.gridItems.length > 1 || (!_consolidateHeader && dashboardStack.filters.length > 0)) {
                var headerArea:Group = new Group();
                var defaultButtonsBox:Group = styleHeaderArea(headerArea);
                addElement(headerArea);
            }
        }
        var container:Group = dashboardStack.consolidateHeaderElements ? buttonsBox : defaultButtonsBox;
        if (container != null) {
            createStackChildren(container);
        }

        if (dashboardStack.filters != null && dashboardStack.filters.length > 0) {
            dashboardFilterArea = new Group();
            dashboardFilterArea.layout = new FlowLayout();
            dashboardFilterArea.addEventListener(FlexEvent.UPDATE_COMPLETE, onUpdateComplete);

            var myFilterColl:ArrayCollection = new ArrayCollection();
            for each (var filterDefinition:FilterDefinition in dashboardStack.filters) {
                var existingFilter:FilterDefinition = stackFilterMap[filterDefinition.qualifiedName()];
                var filterToUse:FilterDefinition;
                if (existingFilter == null) {
                    trace("could not find filter " + filterDefinition.qualifiedName());
                    filterToUse = filterDefinition;
                } else {
                    trace("found existing filter " + filterDefinition.qualifiedName());
                    filterToUse = existingFilter;
                }
                stackFilterMap[filterDefinition.qualifiedName()] = filterToUse;
                myFilterColl.addItem(filterToUse);
                var roleVisible:Boolean = dashboardEditorMetadata.role == 0 || dashboardEditorMetadata.role <= filterToUse.minimumRole;
                if (filterToUse.showOnReportView && roleVisible) {
                    var filterComp:UIComponent = FilterFactory.createTabletFilter(filterToUse, dashboardEditorMetadata.dataSourceID);
                    if (filterComp != null) {
                        filterComp.addEventListener(FilterChangeEvent.FILTER_CHANGE, onFilterChange);
                        dashboardFilterArea.addElement(filterComp);
                    }
                }
            }
            filterMap[elementID] = myFilterColl;
            if (dashboardStack.consolidateHeaderElements) {
                //myFiltersBox.addElement(dashboardFilterArea);
                childFiltersBox.addElement(dashboardFilterArea);
            } else {
                if (_consolidateHeader) {
                    //dashboardFilterArea.width = 800;
                    trace("consolidating headers on parent");
                    if (dashboardStack.filters != null && dashboardStack.filters.length > 0) {
                        _consolidateHeader.percentWidth = 100;
                    }
                    _consolidateHeader.addElement(dashboardFilterArea);
                } else {
                    //dashboardFilterArea.width = 1000;
                    addElement(dashboardFilterArea);
                }
            }
        }
        createComponents();
        currentState = "0";
        compIndex = 0;
    }

    public var stackFilterMap:Object = new Object();

    private function createComponents():void {
        var states:Array = [];
        var comps:Array = [];
        for (var i:int = 0; i < dashboardStack.gridItems.length; i++) {
            var state:State = new State();
            var stackItem:DashboardStackItem = dashboardStack.gridItems.getItemAt(i) as DashboardStackItem;
            var element:DashboardElement = stackItem.dashboardElement;
            var mobileComponent:IDashboardViewComponent = DashboardMobileFactory.createViewUIComponent(element, dashboardEditorMetadata);
            if (mobileComponent is DashboardStackMobileComponent) {
                DashboardStackMobileComponent(mobileComponent).stackFilterMap = this.stackFilterMap;
            }
            comps.push(mobileComponent);
            var filterContainer:Group = new Group();
            if (dashboardStack.consolidateHeaderElements && mobileComponent is DashboardStackMobileComponent) {
                DashboardStackMobileComponent(mobileComponent).consolidateHeader = filterContainer;
            }
            var setProp:SetProperty = new SetProperty(this, "activeChild", mobileComponent);
            var addComp:AddItems = new AddItems();

            addComp.items = mobileComponent;
            addComp.destination = this;

            var retrieveData:RetrieveDataAction = new RetrieveDataAction(mobileComponent, filterMap);
            state.name = String(i);
            if (dashboardStack.consolidateHeaderElements) {
                var addFilters:AddItems = new AddItems();
                addFilters.items = filterContainer;
                addFilters.destination = childFiltersBox;
                state.overrides = [ setProp, addComp, addFilters, retrieveData ];
            } else {
                state.overrides = [ setProp, addComp, retrieveData ];
            }
            states.push(state);
            /**/
        }

        this.states = states;

        /*if (dashboardStack.gridItems.length > 1) {
            var transition:Transition = new Transition();
            transition.fromState = "0";
            transition.toState = "1";
            var sequence:Sequence = new Sequence();
            var fadeOut:Fade = new Fade();
            fadeOut.target = comps[0];
            fadeOut.alphaFrom = 1;
            fadeOut.alphaTo = 0;
            fadeOut.duration = 500;
            var fadeIn:Fade = new Fade();
            fadeIn.alphaFrom = 0;
            fadeIn.alphaTo = 1;
            fadeIn.duration = 500;
            fadeIn.target = comps[1];
            sequence.addChild(fadeOut);
            sequence.addChild(fadeIn);
            transition.effect = sequence;
            this.transitions = [ transition ];
        }*/
    }

    private var dashboardFilterArea:Group;

    private function onUpdateComplete(event:FlexEvent):void {
        var group:Group = Group(event.currentTarget);
        if (group.height != group.contentHeight) {
            group.height = group.contentHeight;
        }
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        var application:UIComponent = FlexGlobals.topLevelApplication as UIComponent;
        if (headerHGroup != null) {
            var childFilters:Group = headerHGroup.getElementAt(0) as Group;
            var filterArea:Group = Group(childFilters.getElementAt(0)).getElementAt(0) as Group;

            var buttons:UIComponent = headerHGroup.getElementAt(1) as UIComponent;
            var buttonsWidth:int = buttons.width;
            trace("buttons width = " + buttonsWidth + " and unscaled width = " + (application.width - 20));

            var width:int = getVisibleRect().width;
            trace("visible rectangle width = " + width);
            filterArea.width = width - buttonsWidth - 40;
            trace("setting to " + filterArea.contentHeight);
            filterArea.height = filterArea.contentHeight;
            childFilters.height = filterArea.contentHeight;
            childFilters.validateSize();
            headerHGroup.height = childFilters.height;

            trace("set child filters width to " + (width - buttonsWidth));
            trace("child's height = " + childFilters.getElementAt(0).height);

        } else {
            if (dashboardFilterArea != null && !_consolidateHeader) {
                dashboardFilterArea.width = getVisibleRect().width - 20;
                dashboardFilterArea.height = dashboardFilterArea.contentHeight;
            }
        }


        if (shareButton != null) {
            shareButton.y = 10;
            shareButton.x = shareButton.parent.width - 175;
        }
        if (logoutButton != null) {
            logoutButton.y = 10;
            logoutButton.x = logoutButton.parent.width - 100;
        }
    }

    private var childFiltersBox:Group;



    private var compIndex:int;

    private var _consolidateHeader:Group = null;

    public function set consolidateHeader(value:Group):void {
        _consolidateHeader = value;
    }

    private function onChange(event:Event):void {
        var button:DashboardButton = event.currentTarget as DashboardButton;
        var index:int = button.elementIndex;
        compIndex = index;
        updateToIndex(index);
    }

    private function updateToIndex(index:int):void {
        currentState = String(index);
    }

    private var filterMap:Object = new Object();

    public var elementID:String;

    private var _activeChild:IDashboardViewComponent;

    public function get activeChild():IDashboardViewComponent {
        return _activeChild;
    }

    public function set activeChild(value:IDashboardViewComponent):void {
        _activeChild = value;
    }

    private function onFilterChange(event:FilterChangeEvent):void {
        //filterMap[elementID] = transformContainer.getFilterDefinitions();
        trace("got filter change event at stack mobile");
        updateAdditionalFilters(filterMap);
        refresh();
    }

    public function updateAdditionalFilters(filterMap:Object):void {
        if (filterMap != null) {
            for (var id:String in filterMap) {
                var filters:Object = filterMap[id];
                if (filters != null) {
                    this.filterMap[id] = filters;
                }
            }
        }
        if (activeChild != null) {
            activeChild.updateAdditionalFilters(this.filterMap);
        }
    }

    public function refresh():void {
        if (activeChild != null) {
            activeChild.refresh();
        }
    }

    public function initialRetrieve():void {
        if (activeChild != null) {
            activeChild.initialRetrieve();
        }
    }

    public function reportCount():ArrayCollection {
        var activeChild:IDashboardViewComponent = IDashboardViewComponent(activeChild);
        return activeChild.reportCount();
    }

    public function obtainPreferredSizeInfo():SizeInfo {
        return null;
    }

    public function toggleFilters(showFilters:Boolean):void {
    }
}
}
