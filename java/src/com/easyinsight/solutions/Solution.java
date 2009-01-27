package com.easyinsight.solutions;

import java.util.List;

/**
 * User: James Boe
 * Date: Aug 28, 2008
 * Time: 12:21:11 AM
 */
public class Solution {

    public static final int APPLICATION = 1;
    public static final int JAVA_JAR = 2;
    public static final int RUBY_GEM = 3;
    public static final int NET_LIB = 4;
    public static final int ESB_PACKAGE = 5;

    private long solutionID;
    private String name;
    private String description;
    private String author = "Easy Insight";
    private String industry;
    private String solutionArchiveName;
    private boolean installable;
    private boolean copyData;
    private long goalTreeID;
    private List<String> tags;

    public boolean isInstallable() {
        return installable;
    }

    public void setInstallable(boolean installable) {
        this.installable = installable;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public long getGoalTreeID() {
        return goalTreeID;
    }

    public void setGoalTreeID(long goalTreeID) {
        this.goalTreeID = goalTreeID;
    }

    public String getSolutionArchiveName() {
        return solutionArchiveName;
    }

    public void setSolutionArchiveName(String solutionArchiveName) {
        this.solutionArchiveName = solutionArchiveName;
    }

    public boolean isCopyData() {
        return copyData;
    }

    public void setCopyData(boolean copyData) {
        this.copyData = copyData;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getSolutionID() {
        return solutionID;
    }

    public void setSolutionID(long solutionID) {
        this.solutionID = solutionID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
