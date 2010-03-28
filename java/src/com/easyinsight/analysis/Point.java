package com.easyinsight.analysis;

/**
* User: jamesboe
* Date: Mar 26, 2010
* Time: 11:28:11 PM
*/
public class Point {
    private String longitude;
    private String latitude;

    Point(String longitude, String latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }
}
