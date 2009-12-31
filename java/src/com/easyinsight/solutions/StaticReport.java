package com.easyinsight.solutions;

import java.util.Date;
import java.util.List;

/**
 * User: jamesboe
 * Date: Dec 14, 2009
 * Time: 7:59:40 PM
 */
public class StaticReport {
    private long reportID;
    private byte[] reportImage;
    private String reportName;
    private String description;
    private long connectionID;
    private List<String> tags;
    private double score;
    private String author;
    private Date creationDate;

    public StaticReport() {
    }

    public StaticReport(long reportID, byte[] reportImage, String reportName, String description, long connectionID,
                        List<String> tags, double score, String author, Date creationDate) {
        this.reportID = reportID;
        this.reportImage = reportImage;
        this.reportName = reportName;
        this.description = description;
        this.connectionID = connectionID;
        this.tags = tags;
        this.score = score;
        this.author = author;
        this.creationDate = creationDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public long getReportID() {
        return reportID;
    }

    public void setReportID(long reportID) {
        this.reportID = reportID;
    }

    public byte[] getReportImage() {
        return reportImage;
    }

    public void setReportImage(byte[] reportImage) {
        this.reportImage = reportImage;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getConnectionID() {
        return connectionID;
    }

    public void setConnectionID(long connectionID) {
        this.connectionID = connectionID;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
