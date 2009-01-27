package com.easyinsight.analysis;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User: James Boe
 * Date: May 19, 2008
 * Time: 11:53:35 AM
 */
@Entity
@Table(name="analysis_tags")
public class Tag implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="analysis_tags_id")
    private Long tagID;
    @Column(name="tag")
    private String tagName;
    @Column(name="use_count")
    private int useCount;

    public Tag() {
    }

    public Tag(String tagName) {
        this.tagName = tagName;
    }

    public int getUseCount() {
        return useCount;
    }

    public void setUseCount(int useCount) {
        this.useCount = useCount;
    }

    public Long getTagID() {
        return tagID;
    }

    public void setTagID(Long tagID) {
        this.tagID = tagID;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        return tagName.equals(tag.tagName);

    }

    public int hashCode() {
        return tagName.hashCode();
    }

    public Tag clone() throws CloneNotSupportedException {
        Tag clonedTag = (Tag) super.clone();
        clonedTag.setTagID(null);
        return clonedTag;
    }
}
