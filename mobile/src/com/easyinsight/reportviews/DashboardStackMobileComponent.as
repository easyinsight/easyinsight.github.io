/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/1/11
 * Time: 2:08 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.reportviews {
import com.easyinsight.dashboard.DashboardElement;
import com.easyinsight.dashboard.DashboardReport;
import com.easyinsight.dashboard.DashboardStack;
import com.easyinsight.dashboard.DashboardStackItem;
import com.easyinsight.skin.ImageLoadEvent;
import com.easyinsight.skin.ImageLoader;

import flash.events.Event;

import mx.collections.ArrayCollection;
import mx.core.UIComponent;

import spark.components.ButtonBar;

import spark.components.Group;
import spark.components.Image;

import spark.components.VGroup;

public class DashboardStackMobileComponent extends VGroup {

    public var dashboardStack:DashboardStack;

    public function DashboardStackMobileComponent() {
        super();
        this.percentWidth = 100;
        this.percentHeight = 100;
    }

    override protected function createChildren():void {
        super.createChildren();
        if (dashboardStack.headerBackground != null) {
            var headerArea:Group = new Group();
            headerArea.setStyle("backgroundColor", dashboardStack.headerBackgroundColor);
            headerArea.setStyle("backgroundAlpha", dashboardStack.headerBackgroundAlpha);
            headerArea.setStyle("horizontalAlign", "center");
            var image:Image = new Image();
            var imageLoader:ImageLoader = new ImageLoader();
            imageLoader.addEventListener(ImageLoadEvent.IMAGE_LOADED, function(event:ImageLoadEvent):void {
                image.width = event.bitmap.width;
                image.height = event.bitmap.height;
                image.source = event.bitmap;
            });
            imageLoader.load(dashboardStack.headerBackground.id);
            headerArea.addElement(image);
        }
        var buttonBar:ButtonBar = new ButtonBar();
        var buttonOptions:ArrayCollection = new ArrayCollection();
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
            buttonOptions.addItem(label);
        }
        buttonBar.dataProvider = buttonOptions;
        buttonBar.addEventListener(Event.CHANGE, onChange);
        addElement(buttonBar);
        var comp:UIComponent = DashboardMobileFactory.createViewUIComponent(DashboardStackItem(dashboardStack.gridItems.getItemAt(0)).dashboardElement, null);
        addElement(comp);
        compIndex = getElementIndex(comp);
    }

    private var compIndex:int;

    private function onChange(event:Event):void {
        var buttonBar:ButtonBar = event.currentTarget as ButtonBar;
        var index:int = buttonBar.selectedIndex;
        removeElementAt(compIndex);
        addElement(DashboardMobileFactory.createViewUIComponent(DashboardStackItem(dashboardStack.gridItems.getItemAt(index)).dashboardElement, null))
    }
}
}
