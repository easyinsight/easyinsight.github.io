package com.easyinsight.kpi;

import com.easyinsight.datafeeds.FeedConsumer;

/**
 * User: jamesboe
 * Date: Feb 3, 2010
 * Time: 10:55:34 AM
 */
public class KPIUser {
    private FeedConsumer feedConsumer;
    private boolean owner;
    private boolean responsible;

    public FeedConsumer getFeedConsumer() {
        return feedConsumer;
    }

    public void setFeedConsumer(FeedConsumer feedConsumer) {
        this.feedConsumer = feedConsumer;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public boolean isResponsible() {
        return responsible;
    }

    public void setResponsible(boolean responsible) {
        this.responsible = responsible;
    }
}
