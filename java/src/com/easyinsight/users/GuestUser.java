package com.easyinsight.users;

import javax.persistence.*;

/**
 * User: James Boe
 * Date: Mar 1, 2009
 * Time: 11:58:11 AM
 */
@Entity
@Table(name="guest_user")
public class GuestUser {
    @Column(name="user_id")
    private Long userID;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="guest_user_id")
    private Long guestUserID;

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getGuestUserID() {
        return guestUserID;
    }

    public void setGuestUserID(Long guestUserID) {
        this.guestUserID = guestUserID;
    }
}
