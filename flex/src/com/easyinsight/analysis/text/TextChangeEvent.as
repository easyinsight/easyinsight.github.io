/**
 * Created by jamesboe on 8/21/14.
 */
package com.easyinsight.analysis.text {
import com.easyinsight.analysis.CustomChangeEvent;

public class TextChangeEvent extends CustomChangeEvent {

    public var text:String;

    public function TextChangeEvent(text:String) {
        this.text = text;
    }
}
}
