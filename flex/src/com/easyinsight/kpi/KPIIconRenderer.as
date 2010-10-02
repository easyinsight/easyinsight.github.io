package com.easyinsight.kpi {

import com.easyinsight.scorecard.KPIRenderer;
import com.easyinsight.util.PrefixManager;

import mx.controls.Image;
import mx.controls.listClasses.IListItemRenderer;

public class KPIIconRenderer extends KPIRenderer implements IListItemRenderer {

    private var image:Image;

    public function KPIIconRenderer() {
        super();
        image = new Image();
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(image);
    }

    [Bindable("dataChange")]    
    override public function set data(val:Object):void {
        super.data = val;
        if (this.kpi.iconImage != null) {
            image.load(PrefixManager.prefix + "/app/assets/icons/32x32/" + this.kpi.iconImage);
        } else {
            image.source = null;
        }
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        image.move(9,0);
        image.setActualSize(32, 32);
    }
}
}