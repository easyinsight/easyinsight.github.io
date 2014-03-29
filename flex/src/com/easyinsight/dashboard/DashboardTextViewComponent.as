package com.easyinsight.dashboard {



import mx.collections.ArrayCollection;
import mx.containers.Box;

import mx.controls.Text;


public class DashboardTextViewComponent extends Box implements IDashboardViewComponent  {

    public var dashboardText:DashboardTextElement;

    public var metadata:DashboardEditorMetadata;

    private var textArea:Text;

    public function DashboardTextViewComponent() {
        super();
        styleName = "myFontStyle";
        setStyle("horizontalAlign", "left");
        setStyle("verticalAlign", "middle");
        /*setStyle("paddingLeft", 10);
         setStyle("paddingTop", 10);
         setStyle("paddingRight", 10);
         setStyle("paddingBottom", 10);*/
        setStyle("backgroundColor", 0xFFFFFF);
        setStyle("backgroundAlpha", 1);
        setStyle("paddingLeft", 0);
        setStyle("paddingRight", 0);
        setStyle("paddingBottom", 10);
    }

    public function stackPopulate(positions:DashboardStackPositions):void {
    }

    public function obtainPreferredSizeInfo():SizeInfo {
        return new SizeInfo(dashboardText.preferredWidth, dashboardText.preferredHeight);
    }

    protected override function createChildren():void {
        super.createChildren();
        textArea = new Text();
        //textArea.setStyle("textAlign", "center");
        textArea.setStyle("fontFamily", "Helvetica Neue, Helvetica");
        textArea.setStyle("fontSize", dashboardText.fontSize);
        textArea.setStyle("color", dashboardText.color);
        setStyle("paddingLeft", dashboardText.paddingLeft);
        setStyle("paddingRight", dashboardText.paddingRight);
        setStyle("paddingTop", dashboardText.paddingTop);
        setStyle("paddingBottom", dashboardText.paddingBottom);
        if (dashboardText.preferredHeight == 0) {
            //textArea.percentHeight = 100;
            this.percentHeight = 100;
        } else {
            //textArea.height = dashboardText.preferredHeight;
            this.height = dashboardText.preferredHeight;
        }
        if (dashboardText.preferredWidth == 0) {
            //textArea.percentWidth = 100;
            this.percentWidth = 100;
        } else {
            textArea.width = dashboardText.preferredWidth;
            this.width = dashboardText.preferredWidth;
        }
        //Alert.show("text area max width = " + textArea.maxWidth);
        //textArea.editable = false;
        //textArea.htmlText = dashboardText.text;
        addChild(textArea);
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        if (textArea != null && (invalidatedTextSize || textArea.maxWidth == 10000)) {
            textArea.maxWidth = unscaledWidth - 40;
            var text:String = dashboardText.html;
            var r1:RegExp = new RegExp("\\</p\\>", "g");
            text = text.replace(r1, "</p>\n");
            var r2:RegExp = new RegExp("\\</ul\\>", "g");
            text = text.replace(r2, "</ul>\n");
            /*var r3:RegExp = new RegExp("\\<h3\\>", "g");
            text = text.replace(r3, "<b>\n");
            var r4:RegExp = new RegExp("\\</h3\\>", "g");
            text = text.replace(r4, "</b>\n");*/
            textArea.htmlText = text;
            invalidatedTextSize = false;
        }
    }

    private var invalidatedTextSize:Boolean = false;

    public function refresh():void {
        invalidatedTextSize = true;
        invalidateDisplayList();
    }

    public function updateAdditionalFilters(filters:Object):void {
    }

    public function initialRetrieve():void {
        invalidatedTextSize = true;
        invalidateDisplayList();
    }

    public function reportCount():ArrayCollection {
        return new ArrayCollection();
    }

    public function toggleFilters(showFilters:Boolean):void {
    }

    public function forceRetrieve():void {
    }
}
}