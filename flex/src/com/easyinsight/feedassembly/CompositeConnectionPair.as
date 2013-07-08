/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 4/29/13
 * Time: 1:16 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.feedassembly {
import com.easyinsight.analysis.AnalysisItem;

public class CompositeConnectionPair {

    public var sourceField:AnalysisItem;
    public var targetField:AnalysisItem;
    public var selected:Boolean;

    public function CompositeConnectionPair() {
    }

    public function get sourceFieldDisplay():String {
        return sourceField.display;
    }

    public function get targetFieldDisplay():String {
        return targetField.display;
    }
}
}
