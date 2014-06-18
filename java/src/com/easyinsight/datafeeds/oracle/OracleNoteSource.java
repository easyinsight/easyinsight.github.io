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

public class OracleNoteSource extends OracleBaseSource {
	public OracleNoteSource() {
        setFeedName("Note");
    }

	public static final String NOTEID = "NoteId";
	public static final String SOURCEOBJECTCODE = "SourceObjectCode";
	public static final String SOURCEOBJECTID = "SourceObjectId";
	public static final String PARTYNAME = "PartyName";
	public static final String NOTETYPECODE = "NoteTypeCode";
	public static final String VISIBILITYCODE = "VisibilityCode";
	public static final String CREATORPARTYID = "CreatorPartyId";
	public static final String NOTEATTRIBUTECATEGORY = "NoteAttributeCategory";
	public static final String NOTEATTRIBUTEUID1 = "NoteAttributeUid1";
	public static final String NOTEATTRIBUTEUID2 = "NoteAttributeUid2";
	public static final String NOTEATTRIBUTEUID3 = "NoteAttributeUid3";
	public static final String NOTEATTRIBUTEUID4 = "NoteAttributeUid4";
	public static final String NOTEATTRIBUTEUID5 = "NoteAttributeUid5";
	public static final String CREATEDBY = "CreatedBy";
	public static final String CREATIONDATE = "CreationDate";
	public static final String LASTUPDATEDATE = "LastUpdateDate";
	public static final String PARTYID = "PartyId";
	public static final String CORPCURRENCYCODE = "CorpCurrencyCode";
	public static final String CURCYCONVRATETYPE = "CurcyConvRateType";
	public static final String CURRENCYCODE = "CurrencyCode";
	public static final String CONTACTRELATIONSHIPID = "ContactRelationshipId";

	protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {		fieldBuilder.addField(NOTEID, new AnalysisDimension());
		fieldBuilder.addField(SOURCEOBJECTCODE, new AnalysisDimension());
		fieldBuilder.addField(SOURCEOBJECTID, new AnalysisDimension());
		fieldBuilder.addField(PARTYNAME, new AnalysisDimension());
		fieldBuilder.addField(NOTETYPECODE, new AnalysisDimension());
		fieldBuilder.addField(VISIBILITYCODE, new AnalysisDimension());
		fieldBuilder.addField(CREATORPARTYID, new AnalysisDimension());
		fieldBuilder.addField(NOTEATTRIBUTECATEGORY, new AnalysisDimension());
		fieldBuilder.addField(NOTEATTRIBUTEUID1, new AnalysisDimension());
		fieldBuilder.addField(NOTEATTRIBUTEUID2, new AnalysisDimension());
		fieldBuilder.addField(NOTEATTRIBUTEUID3, new AnalysisDimension());
		fieldBuilder.addField(NOTEATTRIBUTEUID4, new AnalysisDimension());
		fieldBuilder.addField(NOTEATTRIBUTEUID5, new AnalysisDimension());
		fieldBuilder.addField(CREATEDBY, new AnalysisDimension());
		fieldBuilder.addField(CREATIONDATE, new AnalysisDateDimension());
		fieldBuilder.addField(LASTUPDATEDATE, new AnalysisDateDimension());
		fieldBuilder.addField(PARTYID, new AnalysisDimension());
		fieldBuilder.addField(CORPCURRENCYCODE, new AnalysisDimension());
		fieldBuilder.addField(CURCYCONVRATETYPE, new AnalysisDimension());
		fieldBuilder.addField(CURRENCYCODE, new AnalysisDimension());
		fieldBuilder.addField(CONTACTRELATIONSHIPID, new AnalysisDimension());
	}

@Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
	try {
		DataSet dataSet = new DataSet();
		OracleDataSource oracleDataSource = (OracleDataSource) parentDefinition;
		List<com.easyinsight.datafeeds.oracle.client.Note> list = oracleDataSource.getNote();
		for (com.easyinsight.datafeeds.oracle.client.Note o : list) {
		IRow row = dataSet.createRow();
		row.addValue(keys.get(NOTEID), String.valueOf(o.getNoteId()));
		row.addValue(keys.get(SOURCEOBJECTCODE), o.getSourceObjectCode());
		row.addValue(keys.get(SOURCEOBJECTID), o.getSourceObjectId());
		row.addValue(keys.get(PARTYNAME), o.getPartyName());
		row.addValue(keys.get(NOTETYPECODE), o.getNoteTypeCode());
		row.addValue(keys.get(VISIBILITYCODE), o.getVisibilityCode());
		row.addValue(keys.get(CREATORPARTYID), String.valueOf(o.getCreatorPartyId().getValue()));
		row.addValue(keys.get(NOTEATTRIBUTECATEGORY), o.getNoteAttributeCategory().getValue());
		row.addValue(keys.get(NOTEATTRIBUTEUID1), o.getNoteAttributeUid1().getValue());
		row.addValue(keys.get(NOTEATTRIBUTEUID2), o.getNoteAttributeUid2().getValue());
		row.addValue(keys.get(NOTEATTRIBUTEUID3), o.getNoteAttributeUid3().getValue());
		row.addValue(keys.get(NOTEATTRIBUTEUID4), o.getNoteAttributeUid4().getValue());
		row.addValue(keys.get(NOTEATTRIBUTEUID5), o.getNoteAttributeUid5().getValue());
		row.addValue(keys.get(CREATEDBY), o.getCreatedBy());
		row.addValue(keys.get(CREATIONDATE), getDate(o.getCreationDate()));
		row.addValue(keys.get(LASTUPDATEDATE), getDate(o.getLastUpdateDate()));
		row.addValue(keys.get(PARTYID), String.valueOf(o.getPartyId()));
		row.addValue(keys.get(CORPCURRENCYCODE), o.getCorpCurrencyCode().getValue());
		row.addValue(keys.get(CURCYCONVRATETYPE), o.getCurcyConvRateType().getValue());
		row.addValue(keys.get(CURRENCYCODE), o.getCurrencyCode().getValue());
		row.addValue(keys.get(CONTACTRELATIONSHIPID), String.valueOf(o.getContactRelationshipId().getValue()));
		}

		return dataSet;
	} catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
}

	@Override
    public FeedType getFeedType() {
        return FeedType.ORACLE_NOTE;
    }

}