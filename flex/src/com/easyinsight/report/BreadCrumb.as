/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/28/11
 * Time: 6:35 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.report {
import com.easyinsight.quicksearch.EIDescriptor;

import mx.collections.ArrayCollection;

public class BreadCrumb {

    public var eiDescriptor:EIDescriptor;
    public var filters:ArrayCollection;

    public function BreadCrumb(insightDescriptor:EIDescriptor,
            filters:ArrayCollection) {
        this.eiDescriptor = insightDescriptor;
        this.filters = filters;
    }
}
}
