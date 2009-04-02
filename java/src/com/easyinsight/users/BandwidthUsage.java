package com.easyinsight.users;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Apr 2, 2009
 * Time: 10:49:12 AM
 */
@Entity
@Table(name="bandwidth_usage")
public class BandwidthUsage {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "bandwidth_usage_id")
    private long bandwidthUsageID;

    @Column(name = "account_id")
    private long accountId;

    @Column(name = "used_bandwidth")
    private long usedBandwidth;

    @Column(name = "bandwidth_date")
    @Temporal(TemporalType.DATE)
    private Date bandwidthDate;

    public long getBandwidthUsageID() {
        return bandwidthUsageID;
    }

    public void setBandwidthUsageID(long bandwidthUsageID) {
        this.bandwidthUsageID = bandwidthUsageID;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public long getUsedBandwidth() {
        return usedBandwidth;
    }

    public void setUsedBandwidth(long usedBandwidth) {
        this.usedBandwidth = usedBandwidth;
    }

    public Date getBandwidthDate() {
        return bandwidthDate;
    }

    public void setBandwidthDate(Date bandwidthDate) {
        this.bandwidthDate = bandwidthDate;
    }
}
