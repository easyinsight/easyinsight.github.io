package com.easyinsight.analysis;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Column;

/**
 * User: jamesboe
 * Date: Aug 28, 2009
 * Time: 10:44:15 AM
 */
@Entity
@Table(name="url_link")
@PrimaryKeyJoinColumn(name="link_id")
public class URLLink extends Link {
    @Column(name="url")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
