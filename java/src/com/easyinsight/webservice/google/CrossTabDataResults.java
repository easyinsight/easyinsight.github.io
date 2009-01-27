package com.easyinsight.webservice.google;

import com.easyinsight.core.Value;

import java.util.Collection;
import java.util.Map;
import java.io.Serializable;

/**
 * User: jboe
 * Date: Dec 22, 2007
 * Time: 5:04:09 PM
 */
public class CrossTabDataResults implements Serializable {
    private Collection<Map<String, Value>> results;

    public Collection<Map<String, Value>> getResults() {
        return results;
    }

    public void setResults(Collection<Map<String, Value>> results) {
        this.results = results;
    }
}
