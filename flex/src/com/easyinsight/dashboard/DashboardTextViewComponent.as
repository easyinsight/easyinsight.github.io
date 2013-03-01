package com.easyinsight.dashboard {

import com.easyinsight.util.AutoSizeTextArea;

import mx.collections.ArrayCollection;
import mx.containers.Box;
import mx.controls.Alert;
import mx.controls.Text;
import mx.controls.TextArea;

public class DashboardTextViewComponent extends Box implements IDashboardViewComponent  {

    public var dashboardText:DashboardTextElement;

    public var metadata:DashboardEditorMetadata;

    private var textArea:Text;

    public function DashboardTextViewComponent() {
        super();
        styleName = "myFontStyle";
        setStyle("horizontalAlign", "center");
        setStyle("verticalAlign", "middle");
        /*setStyle("paddingLeft", 10);
        setStyle("paddingTop", 10);
        setStyle("paddingRight", 10);
        setStyle("paddingBottom", 10);*/
        setStyle("backgroundColor", 0xFFFFFF);
        setStyle("backgroundAlpha", 1);
    }

    public function stackPopulate(positions:DashboardStackPositions):void {
        // christmas reset project/program
        // each store gets a specific version of project/program based on dimensions
        // project manager creates an order for a specific store (site)
        // order will have multiple line items
        // whatever parts are needed for display
        // carriers will take shipments to store
        // order will have site, shipment #, etc
        // project is designed to go to some set of stores
        // budgeting mechanism for showing that it's gone to 1000 / 2000
        // volume graph
        // within a shipment
        // heat map of
    }

    public function obtainPreferredSizeInfo():SizeInfo {
        return new SizeInfo(dashboardText.preferredWidth, dashboardText.preferredHeight);
    }

    protected override function createChildren():void {
        super.createChildren();
        textArea = new Text();
        //textArea.setStyle("textAlign", "center");
        textArea.setStyle("fontSize", dashboardText.fontSize);
        textArea.setStyle("color", dashboardText.color);
        if (dashboardText.preferredHeight == 0) {
            textArea.percentHeight = 100;
            this.percentHeight = 100;
        } else {
            //textArea.height = dashboardText.preferredHeight;
            this.height = dashboardText.preferredHeight;
        }
        if (dashboardText.preferredWidth == 0) {
            textArea.percentWidth = 100;
            this.percentWidth = 100;
        } else {
            textArea.width = dashboardText.preferredWidth;
            this.width = dashboardText.preferredWidth;
        }
        //textArea.editable = false;
        textArea.htmlText = dashboardText.text;
        addChild(textArea);
    }

    public function refresh():void {
    }

    public function updateAdditionalFilters(filters:Object):void {
    }

    public function initialRetrieve():void {
    }

    public function reportCount():ArrayCollection {
        return new ArrayCollection();
    }

    public function toggleFilters(showFilters:Boolean):void {
    }
}
}