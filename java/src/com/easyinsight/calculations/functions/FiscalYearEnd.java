package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Value;
import com.easyinsight.security.SecurityUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * User: jamesboe
 * Date: 9/3/14
 * Time: 6:14 AM
 */
public class FiscalYearEnd extends Function {
    @Override
    public Value evaluate() {
        Date startDate = null;
        if (params.size() == 0) {
            startDate = new Date();
        } else {
            Value start = params.get(0);
            if (start.type() == Value.DATE) {
                DateValue dateValue = (DateValue) start;
                startDate = dateValue.getDate();
            }
        }
        Instant instant = startDate.toInstant();
        ZoneId zoneId;
        if (calculationMetadata.getInsightRequestMetadata() == null) {
            zoneId = ZoneId.of("UTC");
        } else {
            zoneId = ZoneId.ofOffset("", ZoneOffset.ofHours(-(calculationMetadata.getInsightRequestMetadata().getUtcOffset() / 60)));
        }
        ZonedDateTime zdt = instant.atZone(zoneId);
        try {
            PreparedStatement ps = calculationMetadata.getConnection().prepareStatement("SELECT fiscal_year_start_month FROM account WHERE account_id = ?");
            ps.setLong(1, SecurityUtil.getAccountID());
            ResultSet rs = ps.executeQuery();
            rs.next();

            int fiscalYearStartMonth = rs.getInt(1);
            int month = zdt.getMonthValue();
            if (fiscalYearStartMonth <= month) {
                zdt = zdt.withMonth(fiscalYearStartMonth).withDayOfMonth(1).plusYears(1).minusDays(1);
            } else {
                zdt = zdt.withMonth(fiscalYearStartMonth).minusYears(1).withDayOfMonth(1).plusYears(1).minusDays(1);
            }
            instant = zdt.toInstant();
            Date endDate = Date.from(instant);
            ps.close();
            return new DateValue(endDate);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getParameterCount() {
        return -1;
    }
}
