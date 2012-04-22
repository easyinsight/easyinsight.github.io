package com.easyinsight.scheduler;

import com.easyinsight.analysis.ReportException;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.email.UserStub;
import com.easyinsight.export.ExportService;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.Account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

/**
 * User: James Boe
 * Date: Apr 15, 2009
 * Time: 5:35:53 PM
 */
@Entity
@Table(name="archive_scheduled_task")
@PrimaryKeyJoinColumn(name="scheduled_task_id")
public class ArchiveDataSourceTask extends ScheduledTask {

    protected void execute(Date now, EIConnection conn) throws Exception {
        PreparedStatement stmt = conn.prepareStatement("SELECT DATA_FEED_ID FROM DATA_FEED WHERE ARCHIVE = ?");
        stmt.setBoolean(1, true);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            new ExportService().archive(rs.getLong(1));
        }
    }
}
