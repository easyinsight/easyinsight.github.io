package com.easyinsight.datafeeds.oracle;

import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.oracle.client.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;

import javax.xml.ws.BindingProvider;
import java.net.URL;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class OracleCategorySummaryRevenueSource extends OracleBaseSource {
	public OracleCategorySummaryRevenueSource() {
        setFeedName("CategorySummaryRevenue");
    }

	public static final String REVNID = "RevnId";
	public static final String REVNNUMBER = "RevnNumber";
	public static final String OPTYID = "OptyId";
	public static final String BUORGID = "BUOrgId";
	public static final String EFFECTIVEDATE = "EffectiveDate";
	public static final String REVNCATEGORYCODE = "RevnCategoryCode";
	public static final String REVNLINETYPECODE = "RevnLineTypeCode";
	public static final String REVNAMOUNTCURCYCODE = "RevnAmountCurcyCode";
	public static final String CRMCURCYCODE = "CrmCurcyCode";
	public static final String CRMCONVERSIONRATETYPE = "CrmConversionRateType";
	public static final String CRMCONVERSIONRATE = "CrmConversionRate";
	public static final String CREATEDBY = "CreatedBy";
	public static final String CREATIONDATE = "CreationDate";
	public static final String CONFLICTID = "ConflictId";
	public static final String LASTUPDATEDBY = "LastUpdatedBy";
	public static final String LASTUPDATEDATE = "LastUpdateDate";
	public static final String LASTUPDATELOGIN = "LastUpdateLogin";
	public static final String USERLASTUPDATEDATE = "UserLastUpdateDate";
	public static final String CRMCATEGORYSUMMARYREVENUE = "CrmCategorySummaryRevenue";

	protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {		fieldBuilder.addField(REVNID, new AnalysisDimension());
		fieldBuilder.addField(REVNNUMBER, new AnalysisDimension());
		fieldBuilder.addField(OPTYID, new AnalysisDimension());
		fieldBuilder.addField(BUORGID, new AnalysisDimension());
		fieldBuilder.addField(EFFECTIVEDATE, new AnalysisDateDimension());
		fieldBuilder.addField(REVNCATEGORYCODE, new AnalysisDimension());
		fieldBuilder.addField(REVNLINETYPECODE, new AnalysisDimension());
		fieldBuilder.addField(REVNAMOUNTCURCYCODE, new AnalysisDimension());
		fieldBuilder.addField(CRMCURCYCODE, new AnalysisDimension());
		fieldBuilder.addField(CRMCONVERSIONRATETYPE, new AnalysisDimension());
		fieldBuilder.addField(CRMCONVERSIONRATE, new AnalysisMeasure());
		fieldBuilder.addField(CREATEDBY, new AnalysisDimension());
		fieldBuilder.addField(CREATIONDATE, new AnalysisDateDimension());
		fieldBuilder.addField(CONFLICTID, new AnalysisDimension());
		fieldBuilder.addField(LASTUPDATEDBY, new AnalysisDimension());
		fieldBuilder.addField(LASTUPDATEDATE, new AnalysisDateDimension());
		fieldBuilder.addField(LASTUPDATELOGIN, new AnalysisDimension());
		fieldBuilder.addField(USERLASTUPDATEDATE, new AnalysisDateDimension());
		fieldBuilder.addField(CRMCATEGORYSUMMARYREVENUE, new AnalysisMeasure());
	}

@Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
	try {
		DataSet dataSet = new DataSet();
		OracleDataSource oracleDataSource = (OracleDataSource) parentDefinition;
		List<com.easyinsight.datafeeds.oracle.client.CategorySummaryRevenue> list = oracleDataSource.getCategorySummaryRevenue();
		for (com.easyinsight.datafeeds.oracle.client.CategorySummaryRevenue o : list) {
		IRow row = dataSet.createRow();
		row.addValue(keys.get(REVNID), String.valueOf(o.getRevnId()));
		row.addValue(keys.get(REVNNUMBER), o.getRevnNumber());
		row.addValue(keys.get(OPTYID), String.valueOf(o.getOptyId().getValue()));
		row.addValue(keys.get(BUORGID), String.valueOf(o.getBUOrgId().getValue()));
		row.addValue(keys.get(EFFECTIVEDATE), getDate(o.getEffectiveDate()));
		row.addValue(keys.get(REVNCATEGORYCODE), o.getRevnCategoryCode().getValue());
		row.addValue(keys.get(REVNLINETYPECODE), o.getRevnLineTypeCode().getValue());
		row.addValue(keys.get(REVNAMOUNTCURCYCODE), o.getRevnAmountCurcyCode().getValue());
		row.addValue(keys.get(CRMCURCYCODE), o.getCrmCurcyCode().getValue());
		row.addValue(keys.get(CRMCONVERSIONRATETYPE), o.getCrmConversionRateType().getValue());
		row.addValue(keys.get(CRMCONVERSIONRATE), getMeasureValue(o.getCrmConversionRate()));
		row.addValue(keys.get(CREATEDBY), o.getCreatedBy());
		row.addValue(keys.get(CREATIONDATE), getDate(o.getCreationDate()));
		row.addValue(keys.get(CONFLICTID), String.valueOf(o.getConflictId()));
		row.addValue(keys.get(LASTUPDATEDBY), o.getLastUpdatedBy());
		row.addValue(keys.get(LASTUPDATEDATE), getDate(o.getLastUpdateDate()));
		row.addValue(keys.get(LASTUPDATELOGIN), o.getLastUpdateLogin().getValue());
		row.addValue(keys.get(USERLASTUPDATEDATE), getDate(o.getUserLastUpdateDate()));
		row.addValue(keys.get(CRMCATEGORYSUMMARYREVENUE), getMeasureValue(o.getCrmCategorySummaryRevenue()));
		}

		return dataSet;
	} catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
}

	@Override
    public FeedType getFeedType() {
        return FeedType.ORACLE_CATEGORYSUMMARYREVENUE;
    }

}