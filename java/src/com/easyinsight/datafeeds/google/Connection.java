package com.easyinsight.datafeeds.google;

/**
* User: jamesboe
* Date: 5/14/12
* Time: 12:51 PM
*/
public class Connection {
    String sourceDatabaseID;
    String sourceDatabaseFieldID;
    String targetDatabaseID;
    String targetDatabaseFieldID;

    Connection(String sourceDatabaseID, String sourceDatabaseFieldID, String targetDatabaseID, String targetDatabaseFieldID) {
        this.sourceDatabaseID = sourceDatabaseID;
        this.sourceDatabaseFieldID = sourceDatabaseFieldID;
        this.targetDatabaseID = targetDatabaseID;
        this.targetDatabaseFieldID = targetDatabaseFieldID;
    }

    @Override
    public String toString() {
        return "Connection{" +
                "sourceDatabaseID='" + sourceDatabaseID + '\'' +
                ", sourceDatabaseFieldID='" + sourceDatabaseFieldID + '\'' +
                ", targetDatabaseID='" + targetDatabaseID + '\'' +
                ", targetDatabaseFieldID='" + targetDatabaseFieldID + '\'' +
                '}';
    }
}
