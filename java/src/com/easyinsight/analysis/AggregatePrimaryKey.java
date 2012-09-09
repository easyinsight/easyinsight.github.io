package com.easyinsight.analysis;

import com.easyinsight.core.Key;

import java.util.List;

/**
 * User: jamesboe
 * Date: 9/9/12
 * Time: 10:39 AM
 */
public class AggregatePrimaryKey extends AggregateKey {

    public AggregatePrimaryKey(Key key, int type, List<FilterDefinition> filters) {
        super(key, type, filters);
    }
}
