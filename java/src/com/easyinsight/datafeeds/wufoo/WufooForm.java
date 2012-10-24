package com.easyinsight.datafeeds.wufoo;

/**
 * User: jamesboe
 * Date: 10/22/12
 * Time: 5:01 PM
 */
public class WufooForm {
    private String id;
    private String name;

    public WufooForm(String id, String name) {
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
