package com.easyinsight.analysis;

import javax.persistence.*;
import java.util.*;
import java.io.Serializable;

/**
 * User: James Boe
 * Date: Jun 13, 2008
 * Time: 11:39:36 PM
 */
@Entity
@Table(name="tag_cloud")
public class TagCloud implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="tag_cloud_id")
    private Long tagCloudID;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name="tag_cloud_to_tag",
        joinColumns = @JoinColumn(name="tag_cloud_id"),
        inverseJoinColumns = @JoinColumn(name="analysis_tags_id"))
    private Collection<Tag> tags = new HashSet<Tag>();

    public Long getTagCloudID() {
        return tagCloudID;
    }

    public void setTagCloudID(Long tagCloudID) {
        this.tagCloudID = tagCloudID;
    }

    public Collection<Tag> getTags() {
        return tags;
    }

    public void setTags(Collection<Tag> tags) {
        this.tags = tags;
    }
}
