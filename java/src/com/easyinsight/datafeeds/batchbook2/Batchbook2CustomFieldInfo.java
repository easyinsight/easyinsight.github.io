package com.easyinsight.datafeeds.batchbook2;

/**
 * User: jamesboe
 * Date: 8/13/12
 * Time: 9:29 AM
 */
public class Batchbook2CustomFieldInfo {
    private String id;
    private String name;

    public Batchbook2CustomFieldInfo(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
