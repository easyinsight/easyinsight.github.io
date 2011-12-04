package com.easyinsight.analysis;

import java.util.List;

/**
* User: jamesboe
* Date: 12/3/11
* Time: 5:24 PM
*/
public class YearStuff {
    private List<String> headers;
    private List<CompareYearsRow> rows;

    YearStuff(List<String> headers, List<CompareYearsRow> rows) {
        this.headers = headers;
        this.rows = rows;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public List<CompareYearsRow> getRows() {
        return rows;
    }
}
