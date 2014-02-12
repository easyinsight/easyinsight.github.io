/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 2/5/14
 * Time: 7:08 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.administration.feed {
import com.easyinsight.analysis.Link;
import com.easyinsight.analysis.ReportFieldExtension;

import flash.events.Event;

public class FieldRuleEvent extends Event {

    public static const FIELD_RULE_ADD:String = "fieldRuleAdd";
    public static const DEFINE_LINK:String = "defineLink";
    public static const DEFINE_EXTENSION:String = "defineExtension";
    public static const EDIT_RULE:String = "editRule";
    public static const FIELD_RULE_EDIT:String = "fieldRuleEdit";

    public var fieldRule:FieldRule;
    public var link:Link;
    public var extension:ReportFieldExtension;

    public function FieldRuleEvent(type:String, fieldRule:FieldRule = null, link:Link = null, extension:ReportFieldExtension = null) {
        super(type, true);
        this.fieldRule = fieldRule;
        this.link = link;
        this.extension = extension;
    }


    override public function clone():Event {
        return new FieldRuleEvent(type, fieldRule, link, extension);
    }
}
}
