package com.easyinsight.storage;

import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;

import java.util.Date;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.Calendar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 * User: James Boe
 * Date: Mar 3, 2009
 * Time: 8:21:20 PM
 */
public class DateDimCache {

    private Map<Long, Long> cache = new WeakHashMap<Long, Long>();

    public long getDateDimID(Date date, Connection conn) throws SQLException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long time = cal.getTimeInMillis();
        Long id = cache.get(time);
        if (id == null) {
            id = getIDFromDatabase(new java.sql.Date(time), conn);
            cache.put(time, id);
        }
        return id;
    }

    private long getIDFromDatabase(java.sql.Date date, Connection conn) throws SQLException {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT DATE_DIMENSION_ID FROM DATE_DIMENSION WHERE DIM_DATE = ?");
        queryStmt.setDate(1, date);
        ResultSet rs = queryStmt.executeQuery();
        rs.next();
        return rs.getLong(1);
    }
}
