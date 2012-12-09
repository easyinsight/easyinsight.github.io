/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/6/12
 * Time: 6:57 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.charts.bubble {
import mx.charts.series.BubbleSeries;
import mx.core.ClassFactory;

/**
 * Determines the horizontal alignment of the text in the bubble label.
 * @default center
 */
[Style(name="textAlign", type="String", enumeration="left,center,right", inherit="yes")]
/**
 * Determines the vertical alignment of the text in the bubble label.
 * @default top
 */
[Style(name="textVAlign", type="String", enumeration="top,middle,bottom", inherit="yes")]

/**
 * Extends BubbleSeries to display bubbles with labels
 */
public class LabeledBubbleSeries extends BubbleSeries
{
    /**
     * Name of data field to be displayed in bubble label
     */
    public var labelField:String = "";

    override public function styleChanged(styleProp:String):void
    {
        super.styleChanged(styleProp);
        if (styleProp=="textAlign" || styleProp=="textVAlign")
        {
            invalidateDisplayList();
        }
    }


    /**
     * Constructor
     */
    public function LabeledBubbleSeries()
    {
        super();
        this.setStyle("itemRenderer", new ClassFactory(LabeledBubbleRenderer));
        this.setStyle("textAlign", "center");
        this.setStyle("textVAlign", "top");
    }

}
}
