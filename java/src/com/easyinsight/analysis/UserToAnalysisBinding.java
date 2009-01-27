package com.easyinsight.analysis;

import javax.persistence.*;

/**
 * User: James Boe
 * Date: Apr 13, 2008
 * Time: 8:33:12 PM
 */
@Entity
@Table(name="user_to_analysis")
public class UserToAnalysisBinding {
    @Column(name="user_id")
    private long userID;
    @Column(name="open")
    private boolean open;
    @Column(name="relationship_type")
    private int relationshipType;
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="user_to_analysis_id")
    private long id;

    public UserToAnalysisBinding() {
    }

    public UserToAnalysisBinding(long userID, int relationshipType) {
        this.userID = userID;
        this.relationshipType = relationshipType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(int relationshipType) {
        this.relationshipType = relationshipType;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
