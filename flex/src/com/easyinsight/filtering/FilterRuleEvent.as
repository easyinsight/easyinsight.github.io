/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 1/21/14
 * Time: 9:23 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {
import flash.events.Event;

public class FilterRuleEvent extends Event {

    public static const RULE_ADDED:String = "ruleAdded";

    public var ruleDefinition:FilterRuleDefinition;

    public function FilterRuleEvent(type:String, ruleDefinition:FilterRuleDefinition) {
        super(type);
        this.ruleDefinition = ruleDefinition;
    }

    override public function clone():Event {
        return new FilterRuleEvent(type, ruleDefinition);
    }
}
}
