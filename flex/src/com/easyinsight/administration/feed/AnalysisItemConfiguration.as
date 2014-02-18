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
import com.easyinsight.listing.Tag;

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

    public function get tagString():String {

        if (tags == null || tags.length == 0) {
            return "";
        } else {
            var str:String = "";
            for (var i:int = 0; i < tags.length; i++) {
                var tag:Tag = tags.getItemAt(i) as Tag;
                str += tag.name;
                if (i < (tags.length - 1)) {
                    str += ", ";
                }
            }
            return str;
        }
    }
}
}
