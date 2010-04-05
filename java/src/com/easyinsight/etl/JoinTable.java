package com.easyinsight.etl;

import java.util.List;

/**
 * User: jamesboe
 * Date: Apr 4, 2010
 * Time: 3:24:08 PM
 */
public class JoinTable {
    private long firstDataSourceID;
    private long secondDataSourceID;
    private String name;
    private long joinTableID;
    private List<JoinTablePair> joinTablePairs;

    public List<JoinTablePair> getJoinTablePairs() {
        return joinTablePairs;
    }

    public void setJoinTablePairs(List<JoinTablePair> joinTablePairs) {
        this.joinTablePairs = joinTablePairs;
    }

    public long getFirstDataSourceID() {
        return firstDataSourceID;
    }

    public void setFirstDataSourceID(long firstDataSourceID) {
        this.firstDataSourceID = firstDataSourceID;
    }

    public long getSecondDataSourceID() {
        return secondDataSourceID;
    }

    public void setSecondDataSourceID(long secondDataSourceID) {
        this.secondDataSourceID = secondDataSourceID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getJoinTableID() {
        return joinTableID;
    }

    public void setJoinTableID(long joinTableID) {
        this.joinTableID = joinTableID;
    }
}
