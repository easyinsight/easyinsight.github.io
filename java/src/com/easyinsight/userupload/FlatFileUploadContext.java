package com.easyinsight.userupload;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.scheduler.FileProcessCreateScheduledTask;
import com.easyinsight.scheduler.ScheduledTask;
import com.easyinsight.security.SecurityUtil;

import java.sql.SQLException;
import java.util.*;

/**
* User: jamesboe
* Date: Mar 27, 2010
* Time: 3:21:36 PM
*/
public class FlatFileUploadContext extends UploadContext {
    private long uploadID;

    public long getUploadID() {
        return uploadID;
    }

    public void setUploadID(long uploadID) {
        this.uploadID = uploadID;
    }

    private transient UploadFormat uploadFormat;
    private transient UserUploadService.RawUploadData rawUploadData;

    @Override
    public String validateUpload(EIConnection conn) throws SQLException {
        rawUploadData = UserUploadService.retrieveRawData(uploadID, conn);
        uploadFormat = new UploadFormatTester().determineFormat(rawUploadData.userData);
        if (uploadFormat == null) {
            return "Sorry, we couldn't figure out what type of file you tried to upload. Supported types are Excel 1997-2008 and delimited text files.";
        } else {
            return null;
        }
    }

    private Map<Key, Set<String>> sampleMap;

    @Override
    public List<AnalysisItem> guessFields() {
        UserUploadAnalysis userUploadAnalysis = uploadFormat.analyze(uploadID, rawUploadData.userData);
        sampleMap = userUploadAnalysis.getSampleMap();
        return userUploadAnalysis.getFields();
    }

    @Override
    public long createDataSource(String name, List<AnalysisItem> analysisItems, EIConnection conn) throws Exception {
        UserUploadService.RawUploadData rawUploadData = UserUploadService.retrieveRawData(uploadID, conn);
        UploadFormat uploadFormat = new UploadFormatTester().determineFormat(rawUploadData.userData);

        FileProcessCreateScheduledTask task = new FileProcessCreateScheduledTask();
        task.setUploadID(uploadID);
        task.setName(name);
        task.setStatus(ScheduledTask.SCHEDULED);
        task.setExecutionDate(new Date());
        task.setUserID(SecurityUtil.getUserID());
        task.setAccountID(SecurityUtil.getAccountID());
        /*if(rawUploadData.getUserData().length > TEN_MEGABYTES) {
            Scheduler.instance().saveTask(task, conn);
            AsyncCreatedEvent e = new AsyncCreatedEvent();
            e.setTask(task);
            e.setUserID(SecurityUtil.getUserID());
            e.setFeedName(name);
            e.setFeedID(0);
            EventDispatcher.instance().dispatch(e);
            uploadResponse = new UploadResponse("Your file has been uploaded and verified, and will be processed shortly.");
        }
        else {*/
        task.createFeed(conn, rawUploadData.getUserData(), uploadFormat, analysisItems);
        return task.getFeedID();
    }

    @Override
    public List<String> getSampleValues(Key key) {
        return new ArrayList<String>(sampleMap.get(key));
    }
}
