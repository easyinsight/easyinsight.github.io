package com.easyinsight.notifications;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Column;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 1, 2009
 * Time: 11:01:00 AM
 */
@Entity
@Table(name="configure_data_feed_todo")
@PrimaryKeyJoinColumn(name = "todo_id")
public class ConfigureDataFeedTodo extends TodoBase {

    public ConfigureDataFeedTodo() {
        setType(TodoBase.CONFIGURE_DATA_SOURCE);
    }

    @Column(name="data_source_id")
    private Long feedID;

    public Long getFeedID() {
        return feedID;
    }

    public void setFeedID(Long feedID) {
        this.feedID = feedID;
    }
}
