/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/2/11
 * Time: 6:02 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.ytd {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSYTDDefinition")]
public class YTDDefinition extends AnalysisDefinition {

    public var measures:ArrayCollection;
    public var timeDimension:AnalysisItem;
    public var ytdID:int;
    public var headerWidth:int = 140;
    public var columnWidth:int = 73;
    public var patternName:String;

    public function YTDDefinition() {
    }

    override public function fromSave(savedDef:AnalysisDefinition):void {
        super.fromSave(savedDef);
        this.ytdID = YTDDefinition(savedDef).ytdID;
    }

    override public function get type():int {
        return AnalysisDefinition.YTD;
    }

    override public function getFields():ArrayCollection {
        return new ArrayCollection();
    }

    override public function populate(fields:ArrayCollection):void {
    }
}
}
