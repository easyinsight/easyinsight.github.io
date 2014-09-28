package com.easyinsight.calculations;

import com.easyinsight.analysis.*;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.export.ExportService;
import com.easyinsight.preferences.ApplicationSkin;
import com.easyinsight.preferences.ApplicationSkinSettings;
import com.easyinsight.preferences.PreferencesService;
import com.easyinsight.security.SecurityUtil;
import org.hibernate.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: 7/21/14
 * Time: 9:50 AM
 */
public class ColorCache implements ICalculationCache {

    private Map<Value, Integer> colorMap = new HashMap<>();
    private AnalysisItem column;
    private CalculationMetadata calculationMetadata;

    public ColorCache(AnalysisItem column, CalculationMetadata calculationMetadata) {
        this.column = column;
        this.calculationMetadata = calculationMetadata;
    }

    @Override
    public void fromDataSet(DataSet dataSet) {

        WSListDefinition tempList = new WSListDefinition();
        tempList.setFilterDefinitions(new ArrayList<>());
        tempList.setAddedItems(calculationMetadata.getReport().getAddedItems());
        tempList.setAddonReports(calculationMetadata.getReport().getAddonReports());
        tempList.setJoinOverrides(calculationMetadata.getReport().getJoinOverrides());
        tempList.setDataFeedID(calculationMetadata.getReport().getDataFeedID());
        tempList.setColumns(Arrays.asList(column));
        EIConnection conn = Database.instance().getConnection();
        try {
            DataSet rowSet = DataService.listDataSet(tempList, new InsightRequestMetadata(), conn);
            ApplicationSkin applicationSkin = getApplicationSkin(conn, calculationMetadata.getReport().getDataFeedID(),
                    calculationMetadata.getReport().getAnalysisID());
            List<MultiColor> multiColors = applicationSkin.getMultiColors();
            List<Integer> colors = createMultiColors(multiColors);
            //Map<String, MultiColor> initColorMap = new HashMap<>();
            boolean emptyValueAdded = false;
            int i = 0;
            for (IRow row : rowSet.getRows()) {
                Value value = row.getValue(column.createAggregateKey());
                if (value.type() == Value.EMPTY) {
                    emptyValueAdded = true;
                }
                Integer color = colors.get(i % colors.size());
                colorMap.put(value, color);
                i++;
            }
            if (!emptyValueAdded) {
                Integer color = colors.get(i % colors.size());
                colorMap.put(new EmptyValue(), color);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    private List<Integer> createMultiColors(List<MultiColor> multiColors) {
        List<Integer> resultColors = new ArrayList<>();
        if (multiColors != null && !multiColors.isEmpty()) {
            MultiColor testColor = multiColors.get(0);
            if (testColor.isColor1StartEnabled()) {
                for (MultiColor color : multiColors) {
                    if (color.isColor1StartEnabled()) {
                        resultColors.add(color.getColor1Start());
                    }
                }
                return resultColors;
            }
        }
        return Arrays.asList(0xa6bc59, 0x597197, 0xd6ab2a, 0xd86068, 0x5d9942,
                0x7a4c6c, 0xF0B400, 0x1E6C0B, 0x00488C, 0x332600, 0xD84000);
    }

    public Integer colorForValue(Value value) {
        return colorMap.get(value);
    }

    private ApplicationSkin getApplicationSkin(EIConnection conn, long sourceID, long baseReportID) throws SQLException {
        ApplicationSkin applicationSkin;
        Session session = Database.instance().createSession(conn);
        try {

            if (SecurityUtil.getAccountID(false) == 0) {
                PreparedStatement uStmt = conn.prepareStatement("SELECT USER.USER_ID, ACCOUNT_ID FROM USER, user_to_analysis WHERE user_to_analysis.analysis_id = ? AND " +
                        "user_to_analysis.user_id = user.user_id");
                uStmt.setLong(1, baseReportID);
                ResultSet uRS = uStmt.executeQuery();
                uRS.next();
                long userID = uRS.getLong(1);
                long accountID = uRS.getLong(2);
                applicationSkin = ApplicationSkinSettings.retrieveSkin(userID, session, accountID);
                uRS.close();
            } else {
                PreparedStatement ps = conn.prepareStatement("SELECT EXCHANGE_AUTHOR FROM ACCOUNT WHERE ACCOUNT_ID = ?");
                ps.setLong(1, SecurityUtil.getAccountID());
                ResultSet rs = ps.executeQuery();
                rs.next();
                boolean exchangeAuthor = rs.getBoolean(1);
                ps.close();

                if (exchangeAuthor) {
                    PreparedStatement typeStmt = conn.prepareStatement("SELECT FEED_TYPE FROM DATA_FEED WHERE DATA_FEED_ID = ?");
                    typeStmt.setLong(1, sourceID);
                    ResultSet typeRS = typeStmt.executeQuery();
                    typeRS.next();
                    int dataSourceType = typeRS.getInt(1);
                    typeStmt.close();
                    applicationSkin = new PreferencesService().getConnectionSkin(dataSourceType);
                    if (applicationSkin == null) {
                        applicationSkin = ApplicationSkinSettings.retrieveSkin(SecurityUtil.getUserID(), session, SecurityUtil.getAccountID());
                    }
                } else {
                    applicationSkin = ApplicationSkinSettings.retrieveSkin(SecurityUtil.getUserID(), session, SecurityUtil.getAccountID());
                }
            }
        } finally {
            session.close();
        }
        return applicationSkin;
    }
}
