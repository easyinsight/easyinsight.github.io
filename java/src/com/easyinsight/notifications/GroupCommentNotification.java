package com.easyinsight.notifications;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Column;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Oct 12, 2009
 * Time: 1:18:34 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="group_comment_notification")
@PrimaryKeyJoinColumn(name="notification_id")
public class GroupCommentNotification extends GroupNotification{
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Column(name="comment")
    private String comment;
}
