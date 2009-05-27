package com.easyinsight.storage;

import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;

import java.util.Date;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.Calendar;
import java.sql.*;

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
        try {
            queryStmt.setDate(1, date);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            } else {
                PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO DATE_DIMENSION (DIM_DATE, DIM_DAY_OF_MONTH," +
                            "DIM_MONTH, DIM_QUARTER_OF_YEAR, DIM_YEAR, DIM_WEEK_OF_YEAR, DIM_DAY_OF_WEEK, DIM_DAY_OF_YEAR) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(date.getTime());
                insertStmt.setDate(1, new java.sql.Date(cal.getTime().getTime()));
                insertStmt.setInt(2, cal.get(Calendar.DAY_OF_MONTH));
                insertStmt.setInt(3, cal.get(Calendar.MONTH));
                int quarterOfYear;
                switch (cal.get(Calendar.MONTH)) {
                    case Calendar.JANUARY:
                    case Calendar.FEBRUARY:
                    case Calendar.MARCH:
                        quarterOfYear = 0;
                        break;
                    case Calendar.APRIL:
                    case Calendar.MAY:
                    case Calendar.JUNE:
                        quarterOfYear = 1;
                        break;
                    case Calendar.JULY:
                    case Calendar.AUGUST:
                    case Calendar.SEPTEMBER:
                        quarterOfYear = 2;
                        break;
                    case Calendar.OCTOBER:
                    case Calendar.NOVEMBER:
                    case Calendar.DECEMBER:
                    default:
                        quarterOfYear = 3;
                        break;
                }
                insertStmt.setInt(4, quarterOfYear);
                insertStmt.setInt(5, cal.get(Calendar.YEAR));
                insertStmt.setInt(6, cal.get(Calendar.WEEK_OF_YEAR));
                insertStmt.setInt(7, cal.get(Calendar.DAY_OF_WEEK));
                insertStmt.setInt(8, cal.get(Calendar.DAY_OF_YEAR));
                insertStmt.execute();
                return Database.instance().getAutoGenKey(insertStmt);
            }
        } finally {
            queryStmt.close();
        }
    }
}
