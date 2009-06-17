package com.easyinsight.scheduler;

import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.datafeeds.FeedConsumer;
import com.easyinsight.datafeeds.IServerDataSourceDefinition;
import com.easyinsight.email.UserStub;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Column;
import java.util.List;
import java.util.Date;
import java.sql.Connection;

/**
 * User: James Boe
 * Date: Apr 15, 2009
 * Time: 5:35:53 PM
 */
@Entity
@Table(name="data_source_query")
@PrimaryKeyJoinColumn(name="scheduled_task_id")
public class DataSourceScheduledTask extends ScheduledTask {

    @Column(name="data_source_id")
    private long dataSourceID;
    private static FeedStorage feedStorage = new FeedStorage();

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    protected void execute(Date now, Connection conn) throws Exception {
        IServerDataSourceDefinition dataSource = (IServerDataSourceDefinition) feedStorage.getFeedDefinitionData(dataSourceID);
        UserStub dataSourceUser = null;
        List<FeedConsumer> owners = dataSource.getUploadPolicy().getOwners();
        for (FeedConsumer owner : owners){
            if (owner.type() == FeedConsumer.USER) {
                dataSourceUser = (UserStub) owner;
            }
        }
        if (dataSourceUser == null) {
            throw new RuntimeException();
        }
        dataSource.refreshData(null, dataSourceUser.getAccountID(), now, conn, null);
    }
}
