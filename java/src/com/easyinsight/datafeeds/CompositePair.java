package com.easyinsight.datafeeds;

/**
 * User: jamesboe
 * Date: 3/24/12
 * Time: 5:54 PM
 */
public class CompositePair {
    private String field1;
    private String field2;

    public CompositePair(String field1, String field2) {
        this.field1 = field1;
        this.field2 = field2;
    }

    public String getField1() {
        return field1;
    }

    public String getField2() {
        return field2;
    }
}
