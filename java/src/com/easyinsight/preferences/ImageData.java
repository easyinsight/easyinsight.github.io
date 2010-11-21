package com.easyinsight.preferences;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: jamesboe
 * Date: Nov 17, 2010
 * Time: 1:33:35 PM
 */
@Entity
@Table(name="")
public class ImageData {
    private String imageName;
    private long userID;

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }
}
