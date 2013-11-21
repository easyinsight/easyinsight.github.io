package com.easyinsight.analysis;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User: jamesboe
 * Date: 11/19/13
 * Time: 12:57 PM
 */
@Entity
@Table(name="hibernate_tag")
public class WeNeedToReplaceHibernateTag implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hibernate_tag_id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name="tag_id")
    private long tagID;

    public long getTagID() {
        return tagID;
    }

    public void setTagID(long tagID) {
        this.tagID = tagID;
    }
}
