package com.easyinsight.filtering {
import mx.collections.ArrayCollection;
import mx.containers.HBox;

public class OrderedFilter extends HBox implements IFilter {

    private var feedID:int;
    private var orderedFilterDefinition:OrderedFilterDefinition;

    public function OrderedFilter(feedID:int) {
        super();
        this.feedID = feedID;
    }

    public function set filterDefinition(filterDefinition:FilterDefinition):void {
        this.orderedFilterDefinition = filterDefinition as OrderedFilterDefinition;
    }

    public function get filterDefinition():FilterDefinition {
        return this.orderedFilterDefinition;
    }

    public function set analysisItems(analysisItems:ArrayCollection):void {
    }

    public function set filterEditable(editable:Boolean):void {
    }

    public function set showLabel(show:Boolean):void {
    }

    public function set loadingFromReport(loading:Boolean):void {
    }
}
}