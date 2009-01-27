package com.easyinsight.stores;

import javax.persistence.*;
import java.util.Collection;

/**
 * User: James Boe
 * Date: May 28, 2008
 * Time: 11:23:23 AM
 */
@Entity
@Table(name="merchant")
public class Merchant {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column (name="merchant_id")
    private Long merchantID;
    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="merchant_id")
    private Collection<UserMerchantBinding> users;
    @Column (name="merchant_name")
    private String name;

    public Collection<UserMerchantBinding> getUsers() {
        return users;
    }

    public void setUsers(Collection<UserMerchantBinding> users) {
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(Long merchantID) {
        this.merchantID = merchantID;
    }
}
