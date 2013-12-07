/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 11/20/13
 * Time: 5:52 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import com.easyinsight.administration.feed.AnalysisItemConfiguration;

import flash.events.Event;

public class SaveExtensionEvent extends Event {

    public static const SAVE_EXTENSION:String = "saveExtension";

    public function SaveExtensionEvent(type:String) {
        super(type);
    }
}
}
