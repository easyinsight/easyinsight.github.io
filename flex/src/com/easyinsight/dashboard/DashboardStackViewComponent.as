package com.easyinsight.dashboard {
import com.easyinsight.analysis.AnalysisDefinition;

import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.containers.VBox;
import mx.containers.ViewStack;
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
        var headerbar:HBox = new HBox();
        headerbar.height = 30;
        headerbar.percentWidth = 100;
        headerbar.setStyle("horizontalAlign", "center");
        viewStack = new ViewStack();
        viewStack.percentHeight = 100;
        viewStack.percentWidth = 100;
        viewStack.creationPolicy = "all";
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
                topButton.label = String(i);
            }
            headerbar.addChild(topButton);
            var comp:UIComponent = report.createViewComponent();
            viewChildren.addItem(comp);
            viewStack.addChild(comp);
        }
        addChild(headerbar);
        addChild(viewStack);
    }
    
    public function refresh(filters:ArrayCollection):void {
        for each (var comp:IDashboardViewComponent in viewChildren) {
            comp.refresh(filters);
        }
    }

    public function retrieveData(refreshAllSources:Boolean = false):void {
        for each (var comp:IDashboardViewComponent in viewChildren) {
            comp.retrieveData(refreshAllSources);
        }
    }
}
}