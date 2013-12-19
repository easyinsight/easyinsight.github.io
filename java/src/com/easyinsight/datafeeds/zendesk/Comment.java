package com.easyinsight.datafeeds.zendesk;

import java.util.Date;

/**
 * User: jamesboe
 * Date: 12/18/13
 * Time: 2:32 PM
 */
public class Comment {
    private long commentID;
    private String commentBody;
    private String commentAuthor;
    private Date createdAt;
    private String commentTicketID;

    public Comment(long commentID, String commentBody, String commentAuthor, Date createdAt, String commentTicketID) {
        this.commentID = commentID;
        this.commentBody = commentBody;
        this.commentAuthor = commentAuthor;
        this.createdAt = createdAt;
        this.commentTicketID = commentTicketID;
    }

    public long getCommentID() {
        return commentID;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public String getCommentAuthor() {
        return commentAuthor;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getCommentTicketID() {
        return commentTicketID;
    }
}
