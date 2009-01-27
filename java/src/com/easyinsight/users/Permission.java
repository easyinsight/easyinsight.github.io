package com.easyinsight.users;

import javax.persistence.*;

/**
 * User: James Boe
 * Date: May 25, 2008
 * Time: 11:47:46 PM
 */
@Entity
@Table(name="user_permission")
public class Permission {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="user_permission_id")
    private Long permissionID;
    @Column(name="permission_name")
    private String name;

    public Long getPermissionID() {
        return permissionID;
    }

    public void setPermissionID(Long permissionID) {
        this.permissionID = permissionID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
