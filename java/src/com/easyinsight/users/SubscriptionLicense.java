package com.easyinsight.users;

import javax.persistence.*;

/**
 * User: James Boe
 * Date: Jun 24, 2008
 * Time: 1:32:56 PM
 */
@Entity
@Table(name="license_subscription")
public class SubscriptionLicense {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="license_subscription_id")
    private long subscriptionLicenseID;
    @Column(name="feed_id")
    private long feedID;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="account_id")
    private Account account;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getSubscriptionLicenseID() {
        return subscriptionLicenseID;
    }

    public void setSubscriptionLicenseID(long subscriptionLicenseID) {
        this.subscriptionLicenseID = subscriptionLicenseID;
    }

    public long getFeedID() {
        return feedID;
    }

    public void setFeedID(long feedID) {
        this.feedID = feedID;
    }
}
