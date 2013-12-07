/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 11/19/13
 * Time: 8:40 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.administration.feed {
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.TextReportFieldExtension;
import com.easyinsight.analysis.ytd.VerticalListReportExtension;
import com.easyinsight.analysis.ytd.YTDReportFieldExtension;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.AnalysisItemConfiguration")]
public class AnalysisItemConfiguration {

    public var analysisItem:AnalysisItem;
    public var tags:ArrayCollection;
    public var textExtension:TextReportFieldExtension;
    public var ytdExtension:YTDReportFieldExtension;
    public var verticalListExtension:VerticalListReportExtension;

    public var selected:Boolean;

    public function AnalysisItemConfiguration() {
    }

    public function get display():String {
        return analysisItem.display;
    }
}
}
