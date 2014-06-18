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

public class OracleRevenuePartnerSource extends OracleBaseSource {
	public OracleRevenuePartnerSource() {
        setFeedName("RevenuePartner");
    }

	public static final String OPTYID = "OptyId";
	public static final String PARTORGPARTYID = "PartOrgPartyId";
	public static final String PARTPROGRAMID = "PartProgramId";
	public static final String PARTTYPECD = "PartTypeCd";
	public static final String REVNID = "RevnId";
	public static final String REVNPARTORGPARTYID = "RevnPartOrgPartyId";
	public static final String DEALESTCLOSEDATE = "DealEstCloseDate";
	public static final String DEALEXPIRATIONDATE = "DealExpirationDate";
	public static final String DEALTYPE = "DealType";
	public static final String PRPARTRESOURCEPARTYID = "PrPartResourcePartyId";
	public static final String REGISTRATIONNUMBER = "RegistrationNumber";
	public static final String PARTYID = "PartyId";
	public static final String PARTYNAME = "PartyName";
	public static final String PARTYNAME1 = "PartyName1";
	public static final String REVNPARTNERNUMBER = "RevnPartnerNumber";
	public static final String LASTUPDATEDATE = "LastUpdateDate";
	public static final String LASTUPDATELOGIN = "LastUpdateLogin";
	public static final String LASTUPDATEDBY = "LastUpdatedBy";
	public static final String CREATIONDATE = "CreationDate";
	public static final String CREATEDBY = "CreatedBy";

	protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {		fieldBuilder.addField(OPTYID, new AnalysisDimension());
		fieldBuilder.addField(PARTORGPARTYID, new AnalysisDimension());
		fieldBuilder.addField(PARTPROGRAMID, new AnalysisDimension());
		fieldBuilder.addField(PARTTYPECD, new AnalysisDimension());
		fieldBuilder.addField(REVNID, new AnalysisDimension());
		fieldBuilder.addField(REVNPARTORGPARTYID, new AnalysisDimension());
		fieldBuilder.addField(DEALESTCLOSEDATE, new AnalysisDateDimension());
		fieldBuilder.addField(DEALEXPIRATIONDATE, new AnalysisDateDimension());
		fieldBuilder.addField(DEALTYPE, new AnalysisDimension());
		fieldBuilder.addField(PRPARTRESOURCEPARTYID, new AnalysisDimension());
		fieldBuilder.addField(REGISTRATIONNUMBER, new AnalysisDimension());
		fieldBuilder.addField(PARTYID, new AnalysisDimension());
		fieldBuilder.addField(PARTYNAME, new AnalysisDimension());
		fieldBuilder.addField(PARTYNAME1, new AnalysisDimension());
		fieldBuilder.addField(REVNPARTNERNUMBER, new AnalysisDimension());
		fieldBuilder.addField(LASTUPDATEDATE, new AnalysisDateDimension());
		fieldBuilder.addField(LASTUPDATELOGIN, new AnalysisDimension());
		fieldBuilder.addField(LASTUPDATEDBY, new AnalysisDimension());
		fieldBuilder.addField(CREATIONDATE, new AnalysisDateDimension());
		fieldBuilder.addField(CREATEDBY, new AnalysisDimension());
	}

@Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
	try {
		DataSet dataSet = new DataSet();
		OracleDataSource oracleDataSource = (OracleDataSource) parentDefinition;
		List<com.easyinsight.datafeeds.oracle.client.RevenuePartner> list = oracleDataSource.getRevenuePartnerPrimary();
		for (com.easyinsight.datafeeds.oracle.client.RevenuePartner o : list) {
		IRow row = dataSet.createRow();
		row.addValue(keys.get(OPTYID), String.valueOf(o.getOptyId()));
		row.addValue(keys.get(PARTORGPARTYID), String.valueOf(o.getPartOrgPartyId()));
		row.addValue(keys.get(PARTPROGRAMID), String.valueOf(o.getPartProgramId().getValue()));
		row.addValue(keys.get(PARTTYPECD), o.getPartTypeCd().getValue());
		row.addValue(keys.get(REVNID), String.valueOf(o.getRevnId()));
		row.addValue(keys.get(REVNPARTORGPARTYID), String.valueOf(o.getRevnPartOrgPartyId()));
		row.addValue(keys.get(DEALESTCLOSEDATE), getDate(o.getDealEstCloseDate()));
		row.addValue(keys.get(DEALEXPIRATIONDATE), getDate(o.getDealExpirationDate()));
		row.addValue(keys.get(DEALTYPE), o.getDealType().getValue());
		row.addValue(keys.get(PRPARTRESOURCEPARTYID), String.valueOf(o.getPrPartResourcePartyId().getValue()));
		row.addValue(keys.get(REGISTRATIONNUMBER), o.getRegistrationNumber().getValue());
		row.addValue(keys.get(PARTYID), String.valueOf(o.getPartyId()));
		row.addValue(keys.get(PARTYNAME), o.getPartyName());
		row.addValue(keys.get(PARTYNAME1), o.getPartyName1());
		row.addValue(keys.get(REVNPARTNERNUMBER), o.getRevnPartnerNumber().getValue());
		row.addValue(keys.get(LASTUPDATEDATE), getDate(o.getLastUpdateDate()));
		row.addValue(keys.get(LASTUPDATELOGIN), o.getLastUpdateLogin().getValue());
		row.addValue(keys.get(LASTUPDATEDBY), o.getLastUpdatedBy().getValue());
		row.addValue(keys.get(CREATIONDATE), getDate(o.getCreationDate()));
		row.addValue(keys.get(CREATEDBY), o.getCreatedBy());
		}

		return dataSet;
	} catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
}

	@Override
    public FeedType getFeedType() {
        return FeedType.ORACLE_REVENUEPARTNER;
    }

}