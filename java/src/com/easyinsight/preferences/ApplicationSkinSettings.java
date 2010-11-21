package com.easyinsight.preferences;

import com.easyinsight.analysis.ReportProperty;
import com.easyinsight.security.SecurityUtil;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: Nov 17, 2010
 * Time: 1:32:30 PM
 */
@Entity
@Table(name="application_skin")
public class ApplicationSkinSettings {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="application_skin_id")
    private long skinID;

    public long getSkinID() {
        return skinID;
    }

    public void setSkinID(long skinID) {
        this.skinID = skinID;
    }

    @Column(name="user_id")
    private long userID;

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "application_skin_to_report_property",
            joinColumns = @JoinColumn(name = "application_skin_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "report_property_id", nullable = false))
    private List<ReportProperty> properties = new ArrayList<ReportProperty>();

    public List<ReportProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<ReportProperty> properties) {
        this.properties = properties;
    }

    public ApplicationSkin toSkin() {
        ApplicationSkin skin = new ApplicationSkin();
        skin.setId(skinID);
        skin.populateProperties(properties);
        return skin;
    }

    public static ApplicationSkin retrieveSkin(long userID, Session session) {
        List results = session.createQuery("from ApplicationSkinSettings where userID = ?").setLong(0, userID).list();
        if (results.size() > 0) {
            ApplicationSkinSettings settings = (ApplicationSkinSettings) results.get(0);
            return settings.toSkin();
        }
        return null;
    }
}
