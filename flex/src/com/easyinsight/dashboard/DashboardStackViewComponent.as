package com.easyinsight.dashboard {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.filtering.TransformContainer;
import com.easyinsight.filtering.TransformsUpdatedEvent;
import com.easyinsight.skin.BackgroundImage;
import com.easyinsight.skin.ImageLoadEvent;
import com.easyinsight.skin.ImageLoader;

import flash.events.Event;

import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.containers.Box;
import mx.containers.HBox;
import mx.containers.VBox;
import mx.containers.ViewStack;
import mx.controls.Alert;
import mx.controls.Button;
import mx.core.UIComponent;
import mx.effects.Effect;

import org.efflex.mx.viewStackEffects.CubePapervision3D;
import org.efflex.mx.viewStackEffects.Fade;
import org.efflex.mx.viewStackEffects.FlipPapervision3D;
import org.efflex.mx.viewStackEffects.Pixelate;
import org.efflex.mx.viewStackEffects.Slide;

public class DashboardStackViewComponent extends VBox implements IDashboardViewComponent {

    public var dashboardStack:DashboardStack;

    public var dashboardEditorMetadata:DashboardEditorMetadata;

    public function DashboardStackViewComponent() {
        super();
        this.percentWidth = 100;
        this.percentHeight = 100;
    }

    private var viewStack:ViewStack;

    private var viewChildren:ArrayCollection;

    private function onButtonClick(event:MouseEvent):void {
        var currentComp:UIComponent = viewStack.selectedChild;
        var targetIndex:int = event.currentTarget.data as int;
        var newComp:UIComponent = viewStack.getChildAt(targetIndex) as UIComponent;
        if (targetIndex > viewStack.selectedIndex) {
            currentComp.setStyle("hideEffect", leftEffect);
            newComp.setStyle("showEffect", rightEffect);
        } else if (targetIndex < viewStack.selectedIndex) {
            currentComp.setStyle("hideEffect", rightEffect);
            newComp.setStyle("showEffect", leftEffect);
        } else {
            return;
        }
        IDashboardViewComponent(newComp).initialRetrieve();
        viewStack.selectedIndex = targetIndex;
    }

    private var leftEffect:Effect;
    private var rightEffect:Effect;

    protected override function createChildren():void {
        super.createChildren();
        if (dashboardStack.effectType == DashboardStack.SLIDE) {
            leftEffect = new Slide();
            leftEffect.duration = dashboardStack.effectDuration;
            Slide(leftEffect).direction = "left";
            rightEffect = new Slide();
            rightEffect.duration = dashboardStack.effectDuration;
            Slide(rightEffect).direction = "right";
        } else if (dashboardStack.effectType == DashboardStack.FADE) {
            leftEffect = new Fade();
            leftEffect.duration = dashboardStack.effectDuration;
            rightEffect = new Fade();
            rightEffect.duration = dashboardStack.effectDuration;
        } else if (dashboardStack.effectType == DashboardStack.PIXELATE) {
            leftEffect = new Pixelate();
            leftEffect.duration = dashboardStack.effectDuration;
            rightEffect = new Pixelate();
            rightEffect.duration = dashboardStack.effectDuration;
        } else if (dashboardStack.effectType == DashboardStack.ROTATE) {
            leftEffect = new CubePapervision3D();
            leftEffect.duration = dashboardStack.effectDuration;
            CubePapervision3D(leftEffect).direction = "left";
            rightEffect = new CubePapervision3D();
            rightEffect.duration = dashboardStack.effectDuration;
            CubePapervision3D(rightEffect).direction = "right";
        } else if (dashboardStack.effectType == DashboardStack.FLIP) {
            leftEffect = new FlipPapervision3D();
            leftEffect.duration = dashboardStack.effectDuration;
            FlipPapervision3D(leftEffect).direction = "left";
            rightEffect = new FlipPapervision3D();
            rightEffect.duration = dashboardStack.effectDuration;
            FlipPapervision3D(rightEffect).direction = "right";
        }
        var headerArea:Box = new Box();
        headerArea.setStyle("backgroundColor", dashboardStack.headerBackgroundColor);
        headerArea.setStyle("backgroundAlpha", dashboardStack.headerBackgroundAlpha);
        headerArea.setStyle("horizontalAlign", "center");
        headerArea.percentWidth = 100;
        var headerBackgroundImage:BackgroundImage = new BackgroundImage();
        headerBackgroundImage.applyCenterScreenLogic = false;
        headerBackgroundImage.useBindings = false;
        var headerbar:HBox = new HBox();
        if (dashboardStack.headerBackground != null) {
            var headerBarLoader:ImageLoader = new ImageLoader();
            headerBarLoader.addEventListener(ImageLoadEvent.IMAGE_LOADED, function(event:ImageLoadEvent):void {
                headerBackgroundImage.width = event.bitmap.width;
                headerBackgroundImage.height = event.bitmap.height;
                headerBackgroundImage.backgroundImageSource = event.bitmap;
            });
            headerBarLoader.load(dashboardStack.headerBackground.id);
        }
        headerbar.percentWidth = 100;
        headerbar.percentHeight = 100;
        headerbar.setStyle("horizontalAlign", "center");
        headerbar.setStyle("verticalAlign", "bottom");
        headerbar.setStyle("paddingBottom", 5);
        headerBackgroundImage.addChild(headerbar);
        headerArea.addChild(headerBackgroundImage);
        viewStack = new ViewStack();
        viewStack.percentHeight = 100;
        viewStack.percentWidth = 100;
        //viewStack.creationPolicy = "all";
        viewChildren = new ArrayCollection();
        for (var i:int = 0; i < dashboardStack.gridItems.length; i++) {
            var stackItem:DashboardStackItem = dashboardStack.gridItems.getItemAt(i) as DashboardStackItem;
            var report:DashboardElement = stackItem.dashboardElement;
            var topButton:Button = new Button();
            topButton.styleName = "grayButton";
            topButton.data = i;
            topButton.addEventListener(MouseEvent.CLICK, onButtonClick);
            if (report is DashboardReport) {
                topButton.label = DashboardReport(report).report.name;
                if (DashboardReport(report).report.reportType == AnalysisDefinition.HEATMAP) {
                    leftEffect = null;
                    rightEffect = null;
                }
            } else {
                if (report.label != null && report.label != "") {
                    topButton.label = report.label;
                } else {
                    topButton.label = String(i);
                }
            }
            if (dashboardStack.gridItems.length > 1) {
                headerbar.addChild(topButton);
            }
            var comp:UIComponent = DashboardElementFactory.createViewUIComponent(report, dashboardEditorMetadata);
            viewChildren.addItem(comp);
            viewStack.addChild(comp);
        }
        addChild(headerArea);
        if (dashboardStack.filters.length > 0) {
            transformContainer = new TransformContainer();
            transformContainer.setStyle("borderStyle", dashboardStack.filterBorderStyle);
            transformContainer.setStyle("borderColor", dashboardStack.filterBorderColor);
            transformContainer.setStyle("backgroundColor", dashboardStack.filterBackgroundColor);
            transformContainer.setStyle("backgroundAlpha", dashboardStack.filterBackgroundAlpha);
            transformContainer.filterEditable = false;
            transformContainer.existingFilters = dashboardStack.filters;
            updateAdditionalFilters(dashboardStack.filters);
            transformContainer.percentWidth = 100;
            transformContainer.setStyle("paddingLeft", 10);
            transformContainer.setStyle("paddingRight", 10);
            transformContainer.setStyle("paddingTop", 10);
            transformContainer.setStyle("paddingBottom", 10);
            transformContainer.reportView = true;
            transformContainer.feedID = dashboardEditorMetadata.dataSourceID;
            transformContainer.addEventListener(TransformsUpdatedEvent.UPDATED_TRANSFORMS, transformsUpdated);
            addChild(transformContainer);
        }
        addChild(viewStack);
    }

    private var transformContainer:TransformContainer;

    private function transformsUpdated(event:Event):void {
        updateAdditionalFilters(transformContainer.getFilterDefinitions());
        refresh(transformContainer.getFilterDefinitions());
    }

    public function updateAdditionalFilters(filters:ArrayCollection):void {
        for each (var comp:IDashboardViewComponent in viewChildren) {
            comp.updateAdditionalFilters(filters);
        }
    }

    public function refresh(filters:ArrayCollection):void {
        IDashboardViewComponent(viewChildren.getItemAt(viewStack.selectedIndex)).refresh(filters);
    }

    public function retrieveData(refreshAllSources:Boolean = false):void {
        IDashboardViewComponent(viewChildren.getItemAt(viewStack.selectedIndex)).retrieveData(refreshAllSources);
    }

    public function initialRetrieve():void {
        IDashboardViewComponent(viewChildren.getItemAt(viewStack.selectedIndex)).initialRetrieve();
    }
}
}