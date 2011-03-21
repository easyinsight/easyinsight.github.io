package com.easyinsight.solutions;

import java.util.List;

/**
 * User: James Boe
 * Date: Aug 28, 2008
 * Time: 12:21:11 AM
 */
public class Solution {

    private long solutionID;
    private int dataSourceType;
    private String name;
    private String industry;
    private String solutionArchiveName;
    private boolean installable;
    private boolean copyData;
    private int solutionTier;
    private List<String> tags;
    private String logoLink;
    private byte[] image;
    private boolean accessible;
    private int category;

    public int getDataSourceType() {
        return dataSourceType;
    }

    public void setDataSourceType(int dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public String getLogoLink() {
        return logoLink;
    }

    public void setLogoLink(String logoLink) {
        this.logoLink = logoLink;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public boolean isAccessible() {
        return accessible;
    }

    public void setAccessible(boolean accessible) {
        this.accessible = accessible;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getSolutionTier() {
        return solutionTier;
    }

    public void setSolutionTier(int solutionTier) {
        this.solutionTier = solutionTier;
    }

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
