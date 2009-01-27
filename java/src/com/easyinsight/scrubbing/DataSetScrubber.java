package com.easyinsight.scrubbing;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.IRow;

import java.util.List;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: May 9, 2008
 * Time: 8:22:39 PM
 */
public class DataSetScrubber {

    public void scrub(DataSet dataSet, List<DataScrub> dataScrubs) {
        List<IScrubber> scrubbers = new ArrayList<IScrubber>();
        for (DataScrub dataScrub : dataScrubs) {
            scrubbers.add(dataScrub.createScrubber());
        }
        for (IRow row : dataSet.getRows()) {
            for (IScrubber scrubber : scrubbers) {
                scrubber.apply(row);
            }
        }
    }
}
