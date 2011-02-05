package com.easyinsight.userupload;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.scheduler.FileProcessCreateScheduledTask;

import com.easyinsight.security.SecurityUtil;

import java.sql.SQLException;
import java.util.*;

/**
* User: jamesboe
* Date: Mar 27, 2010
* Time: 3:21:36 PM
*/
public class FlatFileUploadContext extends UploadContext {
    private byte[] bytes;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    private transient UploadFormat uploadFormat;

    @Override
    public String validateUpload(EIConnection conn) throws SQLException {
        try {
            uploadFormat = new UploadFormatTester().determineFormat(bytes);
        } catch (InvalidFormatException e) {
            return e.getMessage();
        }
        if (uploadFormat == null) {
            return "Sorry, we couldn't figure out what type of file you tried to upload. Supported types are Excel 1997-2008 and delimited text files.";
        } else {
            return null;
        }
    }

    private Map<Key, Set<String>> sampleMap;

    @Override
    public List<AnalysisItem> guessFields(EIConnection conn) throws Exception {
        UserUploadAnalysis userUploadAnalysis = uploadFormat.analyze(bytes);
        sampleMap = userUploadAnalysis.getSampleMap();
        return userUploadAnalysis.getFields();
    }

    @Override
    public long createDataSource(String name, List<AnalysisItem> analysisItems, EIConnection conn, boolean accountVisible) throws Exception {
        UploadFormat uploadFormat = new UploadFormatTester().determineFormat(bytes);

        FileProcessCreateScheduledTask task = new FileProcessCreateScheduledTask();
        task.setName(name);
        task.setUserID(SecurityUtil.getUserID());
        task.setAccountID(SecurityUtil.getAccountID());
        task.createFeed(conn, bytes, uploadFormat, analysisItems, accountVisible);
        return task.getFeedID();
    }

    @Override
    public List<String> getSampleValues(Key key) {
        return new ArrayList<String>(sampleMap.get(key));
    }
}
