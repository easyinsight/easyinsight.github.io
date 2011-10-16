package com.easyinsight.analysis.trend {
import com.easyinsight.analysis.TrendOutcome;
import com.easyinsight.analysis.TrendReportFieldExtension;

import com.easyinsight.framework.Constants;

import mx.controls.Image;
import mx.controls.listClasses.IListItemRenderer;
import mx.core.UIComponent;

public class TrendIconRenderer extends UIComponent implements IListItemRenderer {

    private var image:Image;

    public function TrendIconRenderer() {
        super();
        image = new Image();
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(image);
    }

    private var iconSize:int = 16;

    [Bindable("dataChange")]    
    public function set data(val:Object):void {
        var outcome:TrendOutcome = val as TrendOutcome;
        if (outcome != null) {
            var ext:TrendReportFieldExtension = outcome.measure.reportFieldExtension as TrendReportFieldExtension;
            if (ext != null && ext.iconImage != null) {
                var fontSize:int = getStyle("fontSize");
                if (fontSize < 20) {
                    image.load(Constants.instance().prefix + "/app/assets/icons/16x16/" + ext.iconImage);
                    iconSize = 16;
                } else {
                    image.load(Constants.instance().prefix + "/app/assets/icons/32x32/" + ext.iconImage);
                    iconSize = 32;
                }
            } else {
                image.source = null;
            }
        }
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        var vMargin:int = (this.unscaledHeight - iconSize) / 2;
        var hMargin:int = (this.unscaledWidth - iconSize) / 2;
        image.move(hMargin, vMargin);
        image.setActualSize(iconSize, iconSize);
    }

    public function get data():Object {
        return null;
    }
}
}