package com.easyinsight.dbservice;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: Jan 31, 2009
 * Time: 10:33:29 PM
 */
public class TestQueryResults {
    private List<Map<String, String>> results = new ArrayList<Map<String, String>>();

    public List<Map<String, String>> getResults() {
        return results;
    }

    public void setResults(List<Map<String, String>> results) {
        this.results = results;
    }
}
