package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Value;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.security.SecurityUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.util.Date;

/**
 * User: jamesboe
 * Date: 9/3/14
 * Time: 6:14 AM
 */
public class FiscalYearStart extends Function {
    @Override
    public Value evaluate() {
        ZoneId zoneId = calculationMetadata.getInsightRequestMetadata().createZoneID();
        LocalDate startDate = date();
        try {
            if (calculationMetadata.getConnection() == null) {
                EIConnection conn = Database.instance().getConnection();
                try {
                    PreparedStatement ps = conn.prepareStatement("SELECT fiscal_year_start_month FROM account WHERE account_id = ?");
                    ps.setLong(1, SecurityUtil.getAccountID());
                    ResultSet rs = ps.executeQuery();
                    rs.next();
                    int fiscalYearStartMonth = rs.getInt(1);
                    int month = startDate.getMonthValue();
                    if (fiscalYearStartMonth <= month) {
                        startDate = startDate.withMonth(fiscalYearStartMonth).withDayOfMonth(1);
                    } else {
                        startDate = startDate.withMonth(fiscalYearStartMonth).minusYears(1).withDayOfMonth(1);
                    }
                    Instant instant = startDate.atStartOfDay().atZone(zoneId).toInstant();
                    Date endDate = Date.from(instant);
                    ps.close();
                    return new DateValue(endDate);
                } finally {
                    Database.closeConnection(conn);
                }
            } else {
                PreparedStatement ps = calculationMetadata.getConnection().prepareStatement("SELECT fiscal_year_start_month FROM account WHERE account_id = ?");
                ps.setLong(1, SecurityUtil.getAccountID());
                ResultSet rs = ps.executeQuery();
                rs.next();
                int fiscalYearStartMonth = rs.getInt(1);
                int month = startDate.getMonthValue();
                if (fiscalYearStartMonth <= month) {
                    startDate = startDate.withMonth(fiscalYearStartMonth).withDayOfMonth(1);
                } else {
                    startDate = startDate.withMonth(fiscalYearStartMonth).minusYears(1).withDayOfMonth(1);
                }
                Instant instant = startDate.atStartOfDay().atZone(zoneId).toInstant();
                Date endDate = Date.from(instant);
                ps.close();
                return new DateValue(endDate);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getParameterCount() {
        return -1;
    }
}
