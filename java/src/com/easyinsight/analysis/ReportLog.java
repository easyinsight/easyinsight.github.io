package com.easyinsight.analysis;

import com.easyinsight.config.ConfigLoader;

import java.io.File;
import java.io.FileWriter;

/**
 * User: jamesboe
 * Date: 8/27/12
 * Time: 10:34 AM
 */
public class ReportLog {

    public String log(String log) {
        try {
            String fileName = ConfigLoader.instance().getOutputLogPath() + File.separator + System.currentTimeMillis() + ".html";
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(log);
            fileWriter.flush();
            fileWriter.close();
            return fileName;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
