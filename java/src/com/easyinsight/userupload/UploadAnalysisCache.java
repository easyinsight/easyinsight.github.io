package com.easyinsight.userupload;

import com.easyinsight.dataset.PersistableDataSetForm;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * User: James Boe
 * Date: Jul 23, 2008
 * Time: 6:57:41 PM
 */
public class UploadAnalysisCache {
    private Map<Long, PersistableDataSetForm> dataSetCache = new WeakHashMap<Long, PersistableDataSetForm>();
    private Map<Long, UserUploadAnalysis> analysisCache = new WeakHashMap<Long, UserUploadAnalysis>();

    private static UploadAnalysisCache instance;

    public static UploadAnalysisCache instance() {
        if (instance == null) {
            instance = new UploadAnalysisCache();
        }
        return instance;
    }

    public PersistableDataSetForm getDataSet(long id) {
        return dataSetCache.get(id);
    }

    public void addDataSet(long id, PersistableDataSetForm persistableDataSetForm) {
        dataSetCache.put(id, persistableDataSetForm);
    }

    public UserUploadAnalysis getUploadAnalysis(long id) {
        return analysisCache.get(id);
    }

    public void addUserUploadAnalysis(long id, UserUploadAnalysis userUploadAnalysis) {
        analysisCache.put(id, userUploadAnalysis);
    }
}
