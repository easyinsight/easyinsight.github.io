package com.easyinsight.users;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 2/5/13
 * Time: 10:20 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="reactivatable_user")
public class ReactivatableUser {

    @Column(name="email")
    private String email;

    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;

    @Column(name="company")
    private String company;


    @Column(name="opt_out")
    private boolean optOut;

    @ManyToOne
    @JoinColumn(name = "old_user_id")
    private User oldUser;

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="reactivatable_user_id")
    private long reactivatableUserId;

    @Column(name="reactivation_key")
    private String reactivationKey;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public User getOldUser() {
        return oldUser;
    }

    public void setOldUser(User oldUser) {
        this.oldUser = oldUser;
    }

    public long getReactivatableUserId() {
        return reactivatableUserId;
    }

    public void setReactivatableUserId(long reactivatableUserId) {
        this.reactivatableUserId = reactivatableUserId;
    }

    public String getReactivationKey() {
        return reactivationKey;
    }

    public void setReactivationKey(String reactivationKey) {
        this.reactivationKey = reactivationKey;
    }

    public boolean isOptOut() {
        return optOut;
    }

    public void setOptOut(boolean optOut) {
        this.optOut = optOut;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
