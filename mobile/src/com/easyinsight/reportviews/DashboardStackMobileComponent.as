/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/1/11
 * Time: 2:08 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.reportviews {
import com.easyinsight.dashboard.DashboardEditorMetadata;
import com.easyinsight.dashboard.DashboardElement;
import com.easyinsight.dashboard.DashboardReport;
import com.easyinsight.dashboard.DashboardStack;
import com.easyinsight.dashboard.DashboardStackItem;
import com.easyinsight.dashboard.IDashboardViewComponent;
import com.easyinsight.filter.FilterFactory;
import com.easyinsight.filtering.FilterDefinition;
import com.easyinsight.skin.ImageLoadEvent;
import com.easyinsight.skin.ImageLoader;
import com.easyinsight.util.AnalysisItemFilterTablet;
import com.easyinsight.util.EIComboBox;
import com.easyinsight.util.FilterChangeEvent;
import com.easyinsight.util.FlatDateFilterTablet;
import com.easyinsight.util.SingleValueFilterTablet;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.core.FlexGlobals;
import mx.core.UIComponent;
import mx.effects.Effect;
import mx.events.FlexEvent;
import mx.graphics.SolidColor;

import org.flexlayouts.layouts.FlowLayout;

import spark.components.Group;
import spark.components.HGroup;
import spark.components.Image;

import spark.components.VGroup;
import spark.effects.Fade;
import spark.primitives.Rect;

public class DashboardStackMobileComponent extends VGroup implements IDashboardViewComponent {

    public var dashboardStack:DashboardStack;

    public var dashboardEditorMetadata:DashboardEditorMetadata;

    private var effect:Effect;

    public function DashboardStackMobileComponent() {
        super();
        this.percentWidth = 100;
        this.percentHeight = 100;
        effect = new Fade();
        effect.duration = 1000;
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
        return headerbar;
    }

    private function createStackChildren(headerbar:Group):void {
        if (dashboardStack.selectionType == 'Buttons') {
            if (dashboardStack.gridItems.length > 1) {
                var buttonBar:HGroup = new HGroup();
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
            var myFiltersBox:HGroup = new HGroup();
            headerHGroup.addElement(myFiltersBox);
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
            for each (var filter:FilterDefinition in dashboardStack.filters) {
                var filterComp:UIComponent = FilterFactory.createTabletFilter(filter, dashboardEditorMetadata.dataSourceID);
                if (filterComp != null) {
                    filterComp.addEventListener(FilterChangeEvent.FILTER_CHANGE, onFilterChange);
                    dashboardFilterArea.addElement(filterComp);
                }
            }
            filterMap[elementID] = dashboardStack.filters;
            if (dashboardStack.consolidateHeaderElements) {
                trace("consolidating headers on myself");
                /*if (dashboardStack.filters != null && dashboardStack.filters.length > 1) {
                    myFiltersBox.percentWidth = 100;
                }*/
                myFiltersBox.addElement(dashboardFilterArea);
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
        activeChild = DashboardMobileFactory.createViewUIComponent(DashboardStackItem(dashboardStack.gridItems.getItemAt(0)).dashboardElement, dashboardEditorMetadata);
        if (dashboardStack.consolidateHeaderElements && activeChild is DashboardStackMobileComponent) {
            DashboardStackMobileComponent(activeChild).consolidateHeader = childFiltersBox;
        }
        addElement(activeChild as UIComponent);
        //UIComponent(activeChild).setStyle("removedEffect", effect);
        compIndex = getElementIndex(activeChild as UIComponent);
        trace("comp index = " + compIndex);
    }

    private var dashboardFilterArea:Group;

    private function onUpdateComplete(event:FlexEvent):void {
        trace("WHYYYYYYY " + UIComponent(event.currentTarget).width + " - " + UIComponent(event.currentTarget).height + " - " + UIComponent(event.currentTarget).measuredHeight + " - " + Group(event.currentTarget).contentHeight);
        var group:Group = Group(event.currentTarget);
        if (group.height != group.contentHeight) {
            group.height = group.contentHeight;
        }
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        var application:UIComponent = FlexGlobals.topLevelApplication as UIComponent;
        if (headerHGroup != null) {
            var childFilters:Group = headerHGroup.getElementAt(1) as Group;
            var filterArea:Group = childFilters.getElementAt(0) as Group;

            var buttons:UIComponent = headerHGroup.getElementAt(2) as UIComponent;
            var buttonsWidth:int = buttons.width;
            trace("buttons width = " + buttonsWidth + " and unscaled width = " + (application.width - 20));


            filterArea.width = application.width - buttonsWidth - 40;
            trace("setting to " + filterArea.contentHeight);
            filterArea.height = filterArea.contentHeight;
            //filterArea.invalidateSize();
            //trace("argh = " + filterArea.measuredHeight + " - " + filterArea.contentHeight);
            /*childFilters.validateSize();
            headerHGroup.height = childFilters.height;*/

            trace("set child filters width to " + (application.width - buttonsWidth));
            trace("child's height = " + childFilters.getElementAt(0).height);

        } else {
            if (dashboardFilterArea != null && !_consolidateHeader) {
                dashboardFilterArea.width = application.width - 20;
                dashboardFilterArea.height = dashboardFilterArea.contentHeight;
            }
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
        removeElementAt(compIndex);
        activeChild = DashboardMobileFactory.createViewUIComponent(DashboardStackItem(dashboardStack.gridItems.getItemAt(index)).dashboardElement, dashboardEditorMetadata);
        if (childFiltersBox != null) {
            childFiltersBox.removeAllElements();
            if (dashboardStack.consolidateHeaderElements && activeChild is DashboardStackMobileComponent) {
                DashboardStackMobileComponent(activeChild).consolidateHeader = childFiltersBox;
            }
        }
        /*UIComponent(activeChild).setStyle("addedEffect", effect);
        UIComponent(activeChild).setStyle("removedEffect", effect);*/
        addElementAt(activeChild as UIComponent, compIndex);
        updateAdditionalFilters(filterMap);
        activeChild.initialRetrieve();
    }

    private var filterMap:Object = new Object();

    public var elementID:String;

    private var activeChild:IDashboardViewComponent;

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
        activeChild.updateAdditionalFilters(this.filterMap);
    }

    public function refresh():void {
        activeChild.refresh();
    }

    public function initialRetrieve():void {
        activeChild.initialRetrieve();
    }

    public function reportCount():ArrayCollection {
        return null;
    }
}
}
