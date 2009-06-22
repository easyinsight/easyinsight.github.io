package com.easyinsight.solutions {
import flash.events.Event;

public class SolutionCategoryEvent extends Event{

    public static const CATEGORY_SELECTED:String = "categorySelected";
    public static const CATEGORY_DESELECTED:String = "categoryDeselected";

    public var category:String;

    public function SolutionCategoryEvent(type:String, category:String) {
        super(type);
        this.category = category;
    }

    override public function clone():Event {
        return new SolutionCategoryEvent(category);
    }
}
}