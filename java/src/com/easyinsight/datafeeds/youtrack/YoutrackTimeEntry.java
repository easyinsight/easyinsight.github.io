package com.easyinsight.datafeeds.youtrack;

import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 5/27/14
 * Time: 8:29 AM
 */
public class YoutrackTimeEntry {
    private String issueID;
    private String author;
    private int duration;
    private Value date;

    public YoutrackTimeEntry(String issueID, String author, int duration, Value date) {
        this.issueID = issueID;
        this.author = author;
        this.duration = duration;
        this.date = date;
    }

    public String getIssueID() {
        return issueID;
    }

    public String getAuthor() {
        return author;
    }

    public int getDuration() {
        return duration;
    }

    public Value getDate() {
        return date;
    }
}
