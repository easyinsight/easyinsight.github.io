package com.easyinsight.datafeeds.composite;

/**
 * User: jamesboe
 * Date: 2/19/14
 * Time: 11:58 AM
 */
public class CustomFieldTag {
    private int type;
    private String name;
    private long tagID;

    public CustomFieldTag() {
    }

    public CustomFieldTag(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public long getTagID() {
        return tagID;
    }

    public void setTagID(long tagID) {
        this.tagID = tagID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
