package com.easyinsight.audit;

import com.easyinsight.scorecard.ScorecardDescriptor;

import javax.persistence.*;
import java.util.Date;

/**
 * User: jamesboe
 * Date: 5/13/11
 * Time: 7:50 PM
 */
@Entity
@Table(name="action_scorecard_log")
@PrimaryKeyJoinColumn(name="action_log_id")
public class ActionScorecardLog extends ActionLog {

    public static final int EDIT = 1;
    public static final int VIEW = 2;

    @Column(name="scorecard_id")
    private long scorecardID;

    @Transient
    private ScorecardDescriptor scorecardDescriptor;

    public ActionScorecardLog() {
    }

    public ActionScorecardLog(long userID, int actionType, long scorecardID) {
        super(userID, actionType);
        this.scorecardID = scorecardID;
    }

    public ActionScorecardLog(ScorecardDescriptor scorecardDescriptor, int actionType, Date date) {
        this.scorecardDescriptor = scorecardDescriptor;
        scorecardID = scorecardDescriptor.getId();
        setActionType(actionType);
        setActionDate(date);
    }

    public long getScorecardID() {
        return scorecardID;
    }

    public void setScorecardID(long scorecardID) {
        this.scorecardID = scorecardID;
    }

    public ScorecardDescriptor getScorecardDescriptor() {
        return scorecardDescriptor;
    }

    public void setScorecardDescriptor(ScorecardDescriptor scorecardDescriptor) {
        this.scorecardDescriptor = scorecardDescriptor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ActionScorecardLog that = (ActionScorecardLog) o;

        if (scorecardID != that.scorecardID) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (scorecardID ^ (scorecardID >>> 32));
        return result;
    }
}

