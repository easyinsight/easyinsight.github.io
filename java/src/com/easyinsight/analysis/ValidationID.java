package com.easyinsight.analysis;

/**
* User: jamesboe
* Date: 2/16/14
* Time: 11:15 AM
*/
public class ValidationID {

    public static final int FIELD = 1;
    public static final int FILTER = 2;
    public static final int KEY = 3;

    private int type;
    private long id;

    ValidationID(int type, long id) {
        this.type = type;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ValidationID that = (ValidationID) o;

        if (id != that.id) return false;
        if (type != that.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type;
        result = 31 * result + (int) (id ^ (id >>> 32));
        return result;
    }
}
