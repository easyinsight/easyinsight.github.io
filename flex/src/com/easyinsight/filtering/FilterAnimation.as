/**
 * Created by jamesboe on 11/4/14.
 */
package com.easyinsight.filtering {
import mx.containers.Canvas;
import mx.controls.Image;

public class FilterAnimation extends Canvas {

    [Bindable]
    [Embed(source="../../../../assets/funnel_x32.png")]
    private var filterImage:Class;

    public function FilterAnimation() {
        var img:Image = new Image();
        img.source = filterImage;
        addChild(img);
    }
}
}
