package com.easyinsight.util {
import flash.display.DisplayObject;

import flexlib.containers.FlowBox;

import mx.core.UIComponent;
import mx.events.FlexEvent;

public class EIFlowBox extends FlowBox {
    public function EIFlowBox() {
        super();
        addEventListener(FlexEvent.UPDATE_COMPLETE, adapterFlowBoxUpdateCompleteHandler);
    }

    protected function adapterFlowBoxUpdateCompleteHandler(event:FlexEvent):void
    {
        // resize the FlowBox manually, as the internal calculation doesn't work

        var fb:FlowBox = event.target as FlowBox;

        if (fb != null)
        {
            if (fb.numChildren > 0)
            {
                // default the needed height to the top view metric

                var maxBottom:int = fb.viewMetrics.top;

                // Iterate over the children of the FlowBox to find the bottom-most bottom, so
                // we can determine how big / small we can make the FlowBox.
                // If it's a UIComponent, include it if the includeInLayout property is true.
                // If it's not a UIComponent, include it if the visible property is true.

                for (var idx:int = 0; idx < fb.numChildren; idx++)
                {
                    var displayObject:DisplayObject = fb.getChildAt(idx);

                    if ((displayObject is UIComponent && (displayObject as
                                                          UIComponent).includeInLayout) ||
                        (!(displayObject is UIComponent) && displayObject.visible))
                    {
                        var thisBottom:int = fb.getChildAt(idx).y + fb.getChildAt(idx).height;

                        if (thisBottom > maxBottom)
                            maxBottom = thisBottom;
                    }
                }

                fb.height = maxBottom + fb.viewMetrics.bottom +
                            fb.getStyle("paddingBottom") + 1;
            }
            else
            {
                fb.height = fb.viewMetrics.top + fb.viewMetrics.bottom;
            }
        }
    }
}
}