package com.easyinsight.datafeeds.gnip;

import com.gnipcentral.client.resource.PublisherScope;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Apr 18, 2009
 * Time: 9:56:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class GnipFilter {
    private PublisherScope scope;
    private String publisherName;
    private String filterName;

    public PublisherScope getScope() {
        return scope;
    }

    public void setScope(PublisherScope scope) {
        this.scope = scope;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }
}
