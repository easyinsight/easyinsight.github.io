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

public class OracleRevenueTerritorySource extends OracleBaseSource {
	public OracleRevenueTerritorySource() {
        setFeedName("RevenueTerritory");
    }

	public static final String REVNTERRID = "RevnTerrId";
	public static final String REVNID = "RevnId";
	public static final String TERRITORYID = "TerritoryId";
	public static final String TERRITORYVERSIONID = "TerritoryVersionId";
	public static final String CONFLICTID = "ConflictId";
	public static final String CREATEDBY = "CreatedBy";
	public static final String CREATIONDATE = "CreationDate";
	public static final String LASTUPDATEDATE = "LastUpdateDate";
	public static final String LASTUPDATEDBY = "LastUpdatedBy";
	public static final String LASTUPDATELOGIN = "LastUpdateLogin";
	public static final String USERLASTUPDATEDATE = "UserLastUpdateDate";
	public static final String NAME = "Name";
	public static final String PARTYNAME = "PartyName";
	public static final String PARTYID = "PartyId";
	public static final String NAME1 = "Name1";
	public static final String ORGANIZATIONID = "OrganizationId";
	public static final String EFFECTIVESTARTDATE = "EffectiveStartDate";
	public static final String EFFECTIVEENDDATE = "EffectiveEndDate";
	public static final String ROLENAME = "RoleName";
	public static final String ROLEID = "RoleId";
	public static final String TYPECODE = "TypeCode";
	public static final String FORECASTPARTICIPATIONCODE = "ForecastParticipationCode";
	public static final String ASSIGNMENTTYPE = "AssignmentType";

	protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {		fieldBuilder.addField(REVNTERRID, new AnalysisDimension());
		fieldBuilder.addField(REVNID, new AnalysisDimension());
		fieldBuilder.addField(TERRITORYID, new AnalysisDimension());
		fieldBuilder.addField(TERRITORYVERSIONID, new AnalysisDimension());
		fieldBuilder.addField(CONFLICTID, new AnalysisDimension());
		fieldBuilder.addField(CREATEDBY, new AnalysisDimension());
		fieldBuilder.addField(CREATIONDATE, new AnalysisDateDimension());
		fieldBuilder.addField(LASTUPDATEDATE, new AnalysisDateDimension());
		fieldBuilder.addField(LASTUPDATEDBY, new AnalysisDimension());
		fieldBuilder.addField(LASTUPDATELOGIN, new AnalysisDimension());
		fieldBuilder.addField(USERLASTUPDATEDATE, new AnalysisDateDimension());
		fieldBuilder.addField(NAME, new AnalysisDimension());
		fieldBuilder.addField(PARTYNAME, new AnalysisDimension());
		fieldBuilder.addField(PARTYID, new AnalysisDimension());
		fieldBuilder.addField(NAME1, new AnalysisDimension());
		fieldBuilder.addField(ORGANIZATIONID, new AnalysisDimension());
		fieldBuilder.addField(EFFECTIVESTARTDATE, new AnalysisDateDimension());
		fieldBuilder.addField(EFFECTIVEENDDATE, new AnalysisDateDimension());
		fieldBuilder.addField(ROLENAME, new AnalysisDimension());
		fieldBuilder.addField(ROLEID, new AnalysisDimension());
		fieldBuilder.addField(TYPECODE, new AnalysisDimension());
		fieldBuilder.addField(FORECASTPARTICIPATIONCODE, new AnalysisDimension());
		fieldBuilder.addField(ASSIGNMENTTYPE, new AnalysisDimension());
	}

@Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
	try {
		DataSet dataSet = new DataSet();
		OracleDataSource oracleDataSource = (OracleDataSource) parentDefinition;
		List<com.easyinsight.datafeeds.oracle.client.RevenueTerritory> list = oracleDataSource.getRevenueTerritory();
		for (com.easyinsight.datafeeds.oracle.client.RevenueTerritory o : list) {
		IRow row = dataSet.createRow();
		row.addValue(keys.get(REVNTERRID), String.valueOf(o.getRevnTerrId()));
		row.addValue(keys.get(REVNID), String.valueOf(o.getRevnId()));
		row.addValue(keys.get(TERRITORYID), String.valueOf(o.getTerritoryId()));
		row.addValue(keys.get(TERRITORYVERSIONID), String.valueOf(o.getTerritoryVersionId().getValue()));
		row.addValue(keys.get(CONFLICTID), String.valueOf(o.getConflictId()));
		row.addValue(keys.get(CREATEDBY), o.getCreatedBy());
		row.addValue(keys.get(CREATIONDATE), getDate(o.getCreationDate()));
		row.addValue(keys.get(LASTUPDATEDATE), getDate(o.getLastUpdateDate()));
		row.addValue(keys.get(LASTUPDATEDBY), o.getLastUpdatedBy());
		row.addValue(keys.get(LASTUPDATELOGIN), o.getLastUpdateLogin().getValue());
		row.addValue(keys.get(USERLASTUPDATEDATE), getDate(o.getUserLastUpdateDate()));
		row.addValue(keys.get(NAME), o.getName());
		row.addValue(keys.get(PARTYNAME), o.getPartyName());
		row.addValue(keys.get(PARTYID), String.valueOf(o.getPartyId()));
		row.addValue(keys.get(NAME1), o.getName1());
		row.addValue(keys.get(ORGANIZATIONID), String.valueOf(o.getOrganizationId()));
		row.addValue(keys.get(EFFECTIVESTARTDATE), getDate(o.getEffectiveStartDate()));
		row.addValue(keys.get(EFFECTIVEENDDATE), getDate(o.getEffectiveEndDate()));
		row.addValue(keys.get(ROLENAME), o.getRoleName());
		row.addValue(keys.get(ROLEID), String.valueOf(o.getRoleId()));
		row.addValue(keys.get(TYPECODE), o.getTypeCode().getValue());
		row.addValue(keys.get(FORECASTPARTICIPATIONCODE), o.getForecastParticipationCode().getValue());
		row.addValue(keys.get(ASSIGNMENTTYPE), o.getAssignmentType().getValue());
		}

		return dataSet;
	} catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
}

	@Override
    public FeedType getFeedType() {
        return FeedType.ORACLE_REVENUETERRITORY;
    }

}