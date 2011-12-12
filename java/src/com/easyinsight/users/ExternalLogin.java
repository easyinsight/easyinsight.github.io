package com.easyinsight.users;

import javax.persistence.*;

/**
 * User: jamesboe
 * Date: 3/28/11
 * Time: 7:11 PM
 */
@Entity
@Table(name="external_login")
@Inheritance(strategy= InheritanceType.JOINED)
public abstract class ExternalLogin {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="external_login_id")
    private long externalLoginID;

    public long getExternalLoginID() {
        return externalLoginID;
    }

    public void setExternalLoginID(long externalLoginID) {
        this.externalLoginID = externalLoginID;
    }

    public abstract String login(String userName, String password);

    public abstract String toSSOMessage();
}
