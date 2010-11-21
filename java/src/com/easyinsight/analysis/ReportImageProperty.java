package com.easyinsight.analysis;

import com.easyinsight.preferences.ImageDescriptor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: jamesboe
 * Date: Jun 2, 2010
 * Time: 9:40:34 AM
 */
@Entity
@Table(name="report_image_property")
public class ReportImageProperty extends ReportProperty {
    @Column(name="user_image_id")
    private long imageID;

    @Column(name="image_name")
    private String imageName;

    public ReportImageProperty(String propertyName, ImageDescriptor imageDescriptor) {
        super(propertyName);
        this.imageName = imageDescriptor.getName();
        this.imageID = imageDescriptor.getId();
    }

    public ReportImageProperty() {
    }

    public long getImageID() {
        return imageID;
    }

    public void setImageID(long imageID) {
        this.imageID = imageID;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public ImageDescriptor createImageDescriptor() {
        ImageDescriptor imageDescriptor = new ImageDescriptor();
        imageDescriptor.setId(imageID);
        imageDescriptor.setName(imageName);
        return imageDescriptor;
    }
}
