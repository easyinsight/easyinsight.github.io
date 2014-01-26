/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 1/26/14
 * Time: 11:09 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.administration.feed {
import com.easyinsight.analysis.AnalysisItem;

import flash.events.Event;

public class AddBlankFieldEvent extends Event {

    public static const BLANK_ADD:String = "blankAdd";

    public var item:AnalysisItem;

    public function AddBlankFieldEvent(item:AnalysisItem) {
        super(BLANK_ADD);
        this.item = item;
    }

    override public function clone():Event {
        return new AddBlankFieldEvent(item);
    }
}
}
