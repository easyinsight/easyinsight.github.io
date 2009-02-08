package com.easyinsight.goals;

/**
 * User: James Boe
 * Date: Jan 2, 2009
 * Time: 12:51:29 PM
 */
public class GoalSolution {
    private long solutionID;
    private String solutionName;
    private String solutionArchiveName;

    public String getSolutionArchiveName() {
        return solutionArchiveName;
    }

    public void setSolutionArchiveName(String solutionArchiveName) {
        this.solutionArchiveName = solutionArchiveName;
    }

    public long getSolutionID() {
        return solutionID;
    }

    public void setSolutionID(long solutionID) {
        this.solutionID = solutionID;
    }

    public String getSolutionName() {
        return solutionName;
    }

    public void setSolutionName(String solutionName) {
        this.solutionName = solutionName;
    }
}
