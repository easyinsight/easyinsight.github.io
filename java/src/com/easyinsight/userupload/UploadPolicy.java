package com.easyinsight.userupload;

import com.easyinsight.datafeeds.FeedConsumer;
import com.easyinsight.email.UserStub;

import java.util.List;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: Apr 29, 2008
 * Time: 2:34:50 PM
 */
public class UploadPolicy {
    public static final int PUBLIC = 1;
    public static final int PRIVATE = 2;
    public static final int COMMERCIAL = 3;
    public static final int ACCOUNT = 4;
    public static final int GROUP = 5;

    private boolean publiclyVisible;
    private boolean marketplaceVisible;
    private List<FeedConsumer> owners = new ArrayList<FeedConsumer>();
    private List<FeedConsumer> viewers  = new ArrayList<FeedConsumer>();

    public UploadPolicy() {
    }

    public UploadPolicy(long userID) {
        owners.add(new UserStub(userID, null, null, null));
    }

    public boolean isPubliclyVisible() {
        return publiclyVisible;
    }

    public void setPubliclyVisible(boolean publiclyVisible) {
        this.publiclyVisible = publiclyVisible;
    }

    public boolean isMarketplaceVisible() {
        return marketplaceVisible;
    }

    public void setMarketplaceVisible(boolean marketplaceVisible) {
        this.marketplaceVisible = marketplaceVisible;
    }

    public List<FeedConsumer> getOwners() {
        return owners;
    }

    public void setOwners(List<FeedConsumer> owners) {
        this.owners = owners;
    }

    public List<FeedConsumer> getViewers() {
        return viewers;
    }

    public void setViewers(List<FeedConsumer> viewers) {
        this.viewers = viewers;
    }
}
