package com.easyinsight.users;

import javax.persistence.*;

/**
 * User: jamesboe
 * Date: Oct 25, 2010
 * Time: 10:01:22 AM
 */
@Entity
@Table(name="scenario")
public class Scenario {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="scenario_id")
    private long scenarioID;
    @Column(name="scenario_name")
    private String name;
    @Column(name="scenario_summary")
    private String summary;
    @Column(name="scenario_description")
    private String description;
    @Column(name="account_id")
    private long accountID;
    @Column(name="user_id")
    private long userID;
    @Column(name="scenario_image")
    private String image;
    @Column(name="scenario_key")
    private String scenarioKey;

    public long getScenarioID() {
        return scenarioID;
    }

    public void setScenarioID(long scenarioID) {
        this.scenarioID = scenarioID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getScenarioKey() {
        return scenarioKey;
    }

    public void setScenarioKey(String scenarioKey) {
        this.scenarioKey = scenarioKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public long getAccountID() {
        return accountID;
    }

    public void setAccountID(long accountID) {
        this.accountID = accountID;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
